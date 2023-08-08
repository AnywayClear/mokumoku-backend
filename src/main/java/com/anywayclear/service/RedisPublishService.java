package com.anywayclear.service;

import com.anywayclear.entity.Alarm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublishService {

    @Autowired
    private RedisTemplate<String, Alarm> redisAlarmTemplate;

    /*
     * [publish]
     * 토픽에 알림을 보내면 변환한 후 전송
     * Config에서 설정해준 redisTemplate.converAndSend() 메서드 사용
     */
    public void publish(ChannelTopic topic, Alarm alarm) {
        redisAlarmTemplate.convertAndSend(topic.getTopic(), alarm);
    }
}

