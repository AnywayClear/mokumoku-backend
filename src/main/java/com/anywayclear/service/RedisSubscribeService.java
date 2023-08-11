package com.anywayclear.service;

import com.anywayclear.entity.Alarm;
import com.anywayclear.repository.SSEInMemoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisSubscribeService implements MessageListener {

    private final RedisTemplate<String, Alarm> redisAlarmTemplate;

    private final SSEInMemoryRepository sseRepository;

    private final AlarmService alarmService;

    // ObjectMaper.readValue를 사용해서 JSON을 파싱해서 자바 객체(ChatMessage.Class)로 바꿔줌
    private final ObjectMapper mapper = new ObjectMapper();

    /*
     * [onMessage]
     * 메시지를 subscribe했을 때 수행할 메서드
     * 여기서 발행된 메시지를 읽어 추가 작업 수행
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            Alarm alarm = mapper.readValue(message.getBody(), Alarm.class); // 받은 메시지 Alarm 객체로 역직렬화

            // 받은 알람 SSE 전송
            // 해당 토픽의 SseEmitter 모두 가져옴
            Map<String, SseEmitter> sseEmitters = sseRepository.getListByKeyPrefix(alarm.getSender());
            sseEmitters.forEach(
                    (key, emitter) -> {
                        // 데이터 전송
                        alarmService.sendToClient(emitter, key, "Alarm", alarm);
                    }
            );

            // 레디스 저장 -> 키(발송인 정보) : 값(알람 객체)
            String key = "member:" + alarm.getSender() + ":alarm:" + alarm.getId(); // key 설정 -> member:memberId:alarm:alarmId;
            redisAlarmTemplate.opsForValue().set(key, alarm); // 레디스에 저장
            redisAlarmTemplate.expire(key, 1, TimeUnit.MINUTES); // TTL 설정 ***** 테스트용

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}