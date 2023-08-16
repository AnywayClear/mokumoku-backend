package com.anywayclear.controller;

import com.anywayclear.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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

//    @PostMapping("/send-data")
//    public void sendData(@AuthenticationPrincipal OAuth2User oAuth2User) {
//        String userId = (String) oAuth2User.getAttributes().get("userId");
//        notificationService.notifyTest(userId, "data");
//    }
}
