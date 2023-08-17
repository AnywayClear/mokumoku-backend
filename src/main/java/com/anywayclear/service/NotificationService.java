package com.anywayclear.service;

import com.anywayclear.dto.response.AlarmResponse;
import com.anywayclear.dto.response.AlarmResponseList;
import com.anywayclear.dto.response.AuctionCompleteNotification;
import com.anywayclear.entity.Alarm;
import com.anywayclear.entity.Auction;
import com.anywayclear.entity.Member;
import com.anywayclear.entity.Produce;
import com.anywayclear.exception.CustomException;
import com.anywayclear.exception.ExceptionCode;
import com.anywayclear.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotificationService {
    // 기본 타임아웃 설정
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final EmitterRepository emitterRepository;
    private final ProduceRepository produceRepository;
    private final DibRepository dibRepository;
    private final MemberRepository memberRepository;
    private final SubscribeRepository subscribeRepository;
    private final RedisTemplate<String,Alarm> redisAlarmTemplate;

    public NotificationService(EmitterRepository emitterRepository, ProduceRepository produceRepository, DibRepository dibRepository, MemberRepository memberRepository, SubscribeRepository subscribeRepository, RedisTemplate<String,Alarm> redisAlarmTemplate) {
        this.emitterRepository = emitterRepository;
        this.produceRepository = produceRepository;
        this.dibRepository = dibRepository;
        this.memberRepository = memberRepository;
        this.subscribeRepository = subscribeRepository;
        this.redisAlarmTemplate = redisAlarmTemplate;
    }

    public SseEmitter subscribe(String userId, String lastEventId) {
        String id = userId + "_" + System.currentTimeMillis();
        SseEmitter emitter = createEmitter(id);
        sendToClient(emitter, id,"connection", "EventStream Created. [userId=" + userId + "]");
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(userId);
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(),"sse", entry.getValue()));
        }
        return emitter;
    }

//    public void notifyTest(String userId, Object data) {
//        sendToClient(userId, data);
//    }

    public void notify(String userId, Auction auction) {
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(userId);
        sseEmitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, auction);
                    sendToClient(emitter, key,"sse", AuctionCompleteNotification.toResponse(auction));
                }
        );
    }

    @Transactional(readOnly = true)
    public void pushAlarm(String type, String topicName, LocalDateTime now) { // 알람 전송

        // 알림 수신 목록 불러오기
        Alarm alarm;
        List<String> receiverKeyList;
        if (type.equals("dib")) {
            Produce produce = produceRepository.findById(Long.parseLong(topicName)).orElseThrow(() -> new CustomException(ExceptionCode.INVALID_PRODUCE_ID));
            alarm = Alarm.builder().type(0).senderId(produce.getId().toString()).senderName(produce.getName()).context("경매가 시작되었습니다!").build(); // 알람 객체 생성
            receiverKeyList = dibRepository.findAllByProduce(produce).stream().map(p -> p.getConsumer().getUserId()).collect(Collectors.toList());
        } else {
            Member seller = memberRepository.findByUserId(topicName).orElseThrow(() -> new CustomException(ExceptionCode.INVALID_MEMBER));
            alarm = Alarm.builder().type(1).senderId(seller.getUserId()).senderName(seller.getNickname()).context("새로운 경매 글이 올라왔습니다!").build(); // 알람 객체 생성
            receiverKeyList = subscribeRepository.findAllBySeller(seller).stream().map(m -> m.getConsumer().getUserId()).collect(Collectors.toList());
        }

        // 받은 알람 SSE 전송
        // 해당 토픽의 SseEmitter 모두 가져옴
        receiverKeyList.forEach(
                receiverKey -> {
                    Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(receiverKey);
                    emitterRepository.saveEventCache(receiverKey, AlarmResponse.toResponse(alarm));
                    sseEmitters.forEach((key, emitter) -> {
                        sendToClient(emitter, key, "Alarm", AlarmResponse.toResponse(alarm));
                        String redisKey = "member:" + receiverKey + ":alarm:" + alarm.getId();
                        redisAlarmTemplate.opsForValue().set(redisKey, alarm); // 레디스에 저장
                        redisAlarmTemplate.expire(redisKey, 1, TimeUnit.DAYS); // TTL 설정 ***** 테스트용
                    });
                }

        );

    }

    @Transactional(readOnly = true)
    public AlarmResponseList getAlarmList(String memberId) { // 해당 유저의 알림 리스트 불러오기
        // 패턴 매칭 사용 -> member:memberId:alarm:*

        // [1] 반복문으로 해당 소비자 userId가 키로 포함된 알림 내역 불러오기
        Set<String> keys = redisAlarmTemplate.keys("member:" + memberId + ":alarm:*");
        // [2] 반복문으로 Value(알람 객체) 불러와 저장
        List<Alarm> alarmList = keys.stream().map(k -> redisAlarmTemplate.opsForValue().get(k)).collect(Collectors.toList()); // 해당 키의 알람 리스트 저장
        return new AlarmResponseList(alarmList);
    }
    private void sendToClient(SseEmitter emitter, String id, String name, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name(name)
                    .data(data));
        } catch (IOException e) {
            emitterRepository.deleteById(id);
            emitter.completeWithError(e);
        }

    }

    private SseEmitter createEmitter(String id) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(id, emitter);
        // Emitter가 완료될 때(모든 데이터가 성공적으로 전송된 상태) Emitter를 삭제한다.
        emitter.onCompletion(() -> emitterRepository.deleteById(id)
        );
        // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

        return emitter;
    }
}
