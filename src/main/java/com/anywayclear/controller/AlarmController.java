package com.anywayclear.controller;

import com.anywayclear.dto.response.AlarmResponseList;
import com.anywayclear.service.AlarmService;
import com.anywayclear.service.RedisSubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceSubscription;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alarms")
public class AlarmController {

    // topic에 메시지 발행을 기다리는 리스너
    private final RedisMessageListenerContainer redisMessageListener;

    // 알림 서비스
    private final AlarmService alarmService;

//    private final LettuceSubscription lettuceSubscription;

//    private final RedisSubscribeService redisSubscribeService;

    @PutMapping("/topic/{topicName}")
    public ResponseEntity<Void> createTopic(@PathVariable String topicName) {
        final String topic = alarmService.createTopic(topicName);
        return ResponseEntity.created(URI.create("api/alarms/" + topic)).build();
    }

    @PostMapping("/topic/{topicName}/subscribe")
    public void subscribeTopic(@PathVariable String topicName) {
//        String userId = (String) oAuth2User.getAttributes().get("userId");
        System.out.println("로그인 유저!!!!!!!!!!!!!!!!!!" + SecurityContextHolder.getContext().getAuthentication());
        alarmService.subscribeTopic(topicName);
    }

    @PostMapping("/topic/{topicName}")
    @ResponseStatus(HttpStatus.CREATED)
    public void pushAlarm(@PathVariable String topicName, @RequestParam(name = "sender") String sender, @RequestParam(name = "context") String context) {
        alarmService.pushAlarm(topicName, sender, context);
        System.out.println("로그인 유저!!!!!!!!!!!!!!!!!!" + SecurityContextHolder.getContext().getAuthentication());
    }

    @DeleteMapping("/topic/{topicName}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTopic(@PathVariable String topicName) {
        alarmService.deleteTopic(topicName);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<AlarmResponseList> getAlarmList(@PathVariable String memberId) {
        return ResponseEntity.ok(alarmService.getAlarmList(memberId));
    }
}
