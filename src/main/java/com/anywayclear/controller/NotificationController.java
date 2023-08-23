package com.anywayclear.controller;

import com.anywayclear.dto.response.AlarmResponseList;
import com.anywayclear.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notifications")
@Slf4j
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal OAuth2User oAuth2User,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        String userId = (String) oAuth2User.getAttributes().get("userId");
        log.debug("Last-Event-Id={}", lastEventId);
        return notificationService.subscribe(userId, lastEventId);
    }

    @GetMapping("/list")
    public ResponseEntity<AlarmResponseList> getAlarmList(@AuthenticationPrincipal OAuth2User oAuth2User) {
        String userId = (String) oAuth2User.getAttributes().get("userId");
        return ResponseEntity.ok(notificationService.getAlarmList(userId));
    }
}
