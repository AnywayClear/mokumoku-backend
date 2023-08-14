package com.anywayclear.service;

import com.anywayclear.dto.response.AlarmResponseList;
import com.anywayclear.entity.*;
import com.anywayclear.exception.CustomException;
import com.anywayclear.exception.ExceptionCode;
import com.anywayclear.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmService  {

    // 리포지토리 대신 템플릿 사용
    private final RedisTemplate<String, Alarm> redisAlarmTemplate;

    private final ExecutorService excutor = Executors.newSingleThreadExecutor();

    // 구독 목록 불러오기 위한 서비스
    private final SubscribeRepository subscribeRepository;

    // 찜 목록 불러오기 위한 리포지토리
    private final DibRepository dibRepository;

    // 유저Id로 유저 객체를 찾기 위한 리포지토리
    private final MemberRepository memberRepository;

    private final SSEInMemoryRepository sseRepository;

    private final ProduceRepository produceRepository;

    public SseEmitter createEmitter(OAuth2User oAuth2User, String lastEventId, LocalDateTime now) {
        // 로그인 유저 userId
        String userId = (String) oAuth2User.getAttributes().get("userId");

        // SSE 연결
        // emitter에 키에 토픽, 유저 정보를 담기
        // 알람 내용에 토픽 정보를 넣어 해당 토픽으로 에미터를 검색해 sse 데이터 보내기
        String key = userId + "_" + now;
        SseEmitter emitter;

        if (sseRepository.get(userId).isPresent()) {
            sseRepository.delete(userId);
            emitter = sseRepository.save(key, new SseEmitter(100000L * 45L));
        } else {
            emitter = sseRepository.save(key, new SseEmitter(100000L * 45L));
        }

        // 오류 발생 시 emitter 삭제
        emitter.onCompletion(() -> {
            System.out.println("onCompletion callback");
            sseRepository.delete(userId);  // 만료되면 리스트에서 삭제
        });
        emitter.onTimeout(() -> {
            System.out.println("onTimeout callback");
            // 만료시 Repository에서 삭제
            sseRepository.delete(userId);
        });
        emitter.onError((e) -> {
            System.out.println("onError callback");
            sseRepository.delete(userId);
        });

        // 503 에러 방지 - 더미 이벤트 전송
        sendToClient(emitter, userId, "Connected", "subscribe");
        System.out.println("sse 알림 발송");

        if (!lastEventId.isEmpty()) { // 클라이언트가 미수신한 Event 유실 예방, 연결이 끊겼거나 미수신된 데이터를 다 찾아서 보내준다.
            Map<String, Object> eventCaches = sseRepository.findAllEventCacheByUserId(userId);
            eventCaches.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), "Alarm", entry.getValue()));
        }

        return emitter;
    }

    public void pushAlarm(String type, String topicName) { // 알람 전송

        // 알림 수신 목록 불러오기
        Alarm alarm;
        List<String> receiverKeyList;
        if (type.equals("dib")) {
            alarm = Alarm.builder().sender(topicName).context("경매가 시작되었습니다!").build(); // 알람 객체 생성
            Produce produce = produceRepository.findById(Long.parseLong(topicName)).orElseThrow(() -> new CustomException(ExceptionCode.INVALID_PRODUCE_ID));
            receiverKeyList = dibRepository.findAllByProduce(produce).stream().map(p -> p.getConsumer().getUserId()).collect(Collectors.toList());
            System.out.println("receiverKeyList = " + receiverKeyList);
        } else {
            alarm = Alarm.builder().sender(topicName).context("새로운 경매 글이 올라왔습니다!").build(); // 알람 객체 생성
            Member seller = memberRepository.findByUserId(topicName).orElseThrow(() -> new CustomException(ExceptionCode.INVALID_MEMBER));
            receiverKeyList = subscribeRepository.findAllBySeller(seller).stream().map(m -> m.getConsumer().getUserId()).collect(Collectors.toList());
            System.out.println("receiverKeyList = " + receiverKeyList);
        }

        // 받은 알람 SSE 전송
        // 해당 토픽의 SseEmitter 모두 가져옴
        receiverKeyList.forEach(
                receiverKey -> {
                    Map<String, SseEmitter> sseEmitters = sseRepository.findAllEmitterByUserId(receiverKey);
                    sseEmitters.forEach(
                            (key, emitter) -> {
                                sseRepository.saveEventCache(key, alarm);
                                sendToClient(emitter, key, "Alarm", alarm);
                            }
                    );
                }
        );


        // 레디스 저장 -> 키(발송인 정보) : 값(알람 객체)
        String key = "member:" + alarm.getSender() + ":alarm:" + alarm.getId(); // key 설정 -> member:memberId:alarm:alarmId;
        redisAlarmTemplate.opsForValue().set(key, alarm); // 레디스에 저장
        redisAlarmTemplate.expire(key, 1, TimeUnit.MINUTES); // TTL 설정 ***** 테스트용

    }

    @Transactional(readOnly = true)
    public AlarmResponseList getSubscribeAlarmList(String memberId) { // 해당 유저의 알림 리스트 불러오기
        // 패턴 매칭 사용 -> member:memberId:alarm:*

        // [1] 유저의 구독 목록 불러오기
        Member member = memberRepository.findByUserId(memberId).orElseThrow(() -> new CustomException(ExceptionCode.INVALID_MEMBER));
        List<Subscribe> subscribeList = subscribeRepository.findAllByConsumer(member);
        Set<String> keys = new HashSet<>();
        for (Subscribe subscribe : subscribeList) {
            String seller = subscribe.getSeller().getUserId(); // Response 객체에서 sender 필드 값을 추출
            keys.add(seller); // keys 집합에 sender 값 추가
        }
        // [2] 반복문으로 해당 판매자 userId가 키로 포함된 알림 내역 불러오기
        Set<String> subKeys = new HashSet<>(); // Set을 사용하여 중복된 값 제거
        for (String key : keys) {
            subKeys.addAll(redisAlarmTemplate.keys("member:" + key + ":alarm:*"));
        }

        // [3] 반복문으로 Value(알람 객체) 불러와 저장
        List<Alarm> alarmList = subKeys.stream().map(k -> redisAlarmTemplate.opsForValue().get(k)).collect(Collectors.toList()); // 해당 키의 알람 리스트 저장
        return new AlarmResponseList(alarmList);
    }

    @Transactional(readOnly = true)
    public AlarmResponseList getDibAlarmList(String memberId) { // 해당 유저의 알림 리스트 불러오기
        // 패턴 매칭 사용 -> member:memberId:alarm:*

        // [1] 유저의 찜 목록 불러오기
        Member member = memberRepository.findByUserId(memberId).orElseThrow(() -> new CustomException(ExceptionCode.INVALID_MEMBER));
        List<Dib> dibList = dibRepository.findAllByConsumer(member);
        Set<String> keys = new HashSet<>(); // Set을 사용하여 중복된 값 제거
        for (Dib dib : dibList) {
            String produce = dib.getProduce().getId().toString(); // Response 객체에서 sender 필드 값을 추출
            keys.add(produce); // keys 집합에 sender 값 추가
        }

        // [2] 반복문으로 해당 경매글의 Id가 키로 포함된 알림 내역 불러오기
        Set<String> Dibskeys = new HashSet<>();
        for (String key : keys) {
            Dibskeys.addAll(redisAlarmTemplate.keys("member:" + key + ":alarm:*"));
        }

        // [3] 반복문으로 Value(알람 객체) 불러와 저장
        List<Alarm> alarmList = Dibskeys.stream().map(k -> redisAlarmTemplate.opsForValue().get(k)).collect(Collectors.toList()); // 해당 키의 알람 리스트 저장
        return new AlarmResponseList(alarmList);
    }

    public void sendToClient(SseEmitter emitter, String key, String name, Object data) {
        log.info("key={}, name={}, message={}",key, name, data);

        excutor.execute(() -> {
            try {
                emitter.send(SseEmitter.event()
                        .id(key)
                        .name(name)
                        .data(data)
                        .reconnectTime(0));
                log.info("data 전달 성공");
                emitter.send("더미 데이터");
                log.info("더미데이터 전달 성공");
                emitter.complete();
                sseRepository.delete(key);
            } catch (IOException e) {
                sseRepository.delete(key);
                emitter.completeWithError(e);
                log.info("예외 발생");
            }
        });
    }
}
