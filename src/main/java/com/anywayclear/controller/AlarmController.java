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
                                                @RequestHeader(value = "Last-Event_ID", required = false, defaultValue = "") String lastEventId) {
        return new ResponseEntity<>(alarmService.createEmitter(oAuth2User, lastEventId), HttpStatus.OK);
    }

    @PostMapping("type/{type}/topic/{topicName}")
    @ResponseStatus(HttpStatus.CREATED)
    public void pushAlarm(@PathVariable("type") String type, @PathVariable("topicName") String topicName) {
        alarmService.pushAlarm(type, topicName, LocalDateTime.now());
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<AlarmResponseList> getAlarmList(@PathVariable String memberId) {
        return ResponseEntity.ok(alarmService.getAlarmList(memberId));
    }
}
