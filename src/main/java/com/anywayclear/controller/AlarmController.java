package com.anywayclear.controller;

import com.anywayclear.dto.response.AlarmResponseList;
import com.anywayclear.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alarms")
public class AlarmController {

    // 알림 서비스
    private final AlarmService alarmService;

    @PostMapping("/topic/{topicName}")
    @ResponseStatus(HttpStatus.CREATED)
    public void pushAlarm(@PathVariable String topicName, @RequestParam(name = "context") String context) {
        alarmService.pushAlarm(topicName, context);
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
