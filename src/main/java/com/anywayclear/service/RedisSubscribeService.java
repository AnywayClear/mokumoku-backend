package com.anywayclear.service;

import com.anywayclear.entity.Alarm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisSubscribeService implements MessageListener {

    private final RedisTemplate<String, Alarm> redisTemplate;

    // ObjectMaper.readValue를 사용해서 JSON을 파싱해서 자바 객체(ChatMessage.Class)로 바꿔줌
    private final ObjectMapper mapper = new ObjectMapper();

//    private final String userId;

//    public RedisSubscribeService(RedisTemplate<String, Alarm> redisTemplate, String userId) {
//        this.redisTemplate = redisTemplate;
//        this.userId = userId;
//    }


    /*
     * [onMessage]
     * 메시지를 subscribe했을 때 수행할 메서드
     * 여기서 발행된 메시지를 읽어 추가 작업 수행
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            System.out.println("try 진입");
            Alarm alarm = mapper.readValue(message.getBody(), Alarm.class); // 받은 메시지 Alarm 객체로 역직렬화
            String key = "member:" + "receiver" + ":alarm:" + alarm.getId(); // key 설정 -> member:memberId:alarm:alarmId
            System.out.println("RedisSubscribeService.onMessage LoginUser = " + SecurityContextHolder.getContext().getAuthentication());
            redisTemplate.opsForValue().set(key, alarm); // 레디스에 저장
            redisTemplate.expire(key, 1, TimeUnit.MINUTES); // TTL 설정
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}