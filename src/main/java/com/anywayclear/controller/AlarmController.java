package com.anywayclear.controller;

import com.anywayclear.dto.response.AlarmResponseList;
import com.anywayclear.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alarms")
public class AlarmController {

    // 알림 서비스
    private final AlarmService alarmService;


    @PutMapping("/topic/{topicName}")
    public ResponseEntity<Void> createTopic(@PathVariable String topicName) {
        final String topic = alarmService.createTopic(topicName);
        return ResponseEntity.created(URI.create("api/alarms/" + topic)).build();
    }

    @PostMapping("/topic/{topicName}/subscribe")
    public void subscribeTopic(@PathVariable String topicName) {
        alarmService.subscribeTopic(topicName);
    }

    @PostMapping("/topic/{topicName}")
    @ResponseStatus(HttpStatus.CREATED)
    public void pushAlarm(@PathVariable String topicName, @RequestParam(name = "sender") String sender, @RequestParam(name = "context") String context) {
        alarmService.pushAlarm(topicName, sender, context);
    }

    @DeleteMapping("/topic/{topicName}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTopic(@PathVariable String topicName) {
        alarmService.deleteTopic(topicName);
    }

    @GetMapping("/{memberId}/subs")
    public ResponseEntity<AlarmResponseList> getSubscribeAlarmList(@PathVariable String memberId) {
        return ResponseEntity.ok(alarmService.getSubscribeAlarmList(memberId));
    }
    @GetMapping("/{memberId}/dibs")
    public ResponseEntity<AlarmResponseList> getDibAlarmList(@PathVariable String memberId) {
        return ResponseEntity.ok(alarmService.getDibAlarmList(memberId));
    }
}
