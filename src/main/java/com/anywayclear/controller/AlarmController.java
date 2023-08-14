package com.anywayclear.controller;

import com.anywayclear.dto.response.AlarmResponseList;
import com.anywayclear.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alarms")
public class AlarmController {

    // 알림 서비스
    private final AlarmService alarmService;

    @GetMapping(produces = "text/event-stream")
    public ResponseEntity<SseEmitter> createEmitter(@AuthenticationPrincipal OAuth2User oAuth2User,
                                                @RequestHeader(value = "Last-Event_ID", required = false) String lastEventId, HttpServletResponse response) {
        return new ResponseEntity<>(alarmService.createEmitter(oAuth2User, lastEventId, LocalDateTime.now()), HttpStatus.OK);
    }

    @PostMapping("/{type}/{topicName}")
    @ResponseStatus(HttpStatus.CREATED)
    public void pushAlarm(@PathVariable("type") String type, @PathVariable("topiceName") String topicName) {
        alarmService.pushAlarm(topicName, type);
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
