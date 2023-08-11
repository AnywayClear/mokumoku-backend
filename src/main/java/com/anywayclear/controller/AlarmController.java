package com.anywayclear.controller;

import com.anywayclear.dto.response.AlarmResponseList;
import com.anywayclear.service.AlarmService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alarms")
public class AlarmController {

    // 알림 서비스
    private final AlarmService alarmService;

//    @PutMapping("/topic/{topicName}")
//    public ResponseEntity<Void> createTopic(@PathVariable String topicName) {
//        final String topic = alarmService.createTopic(topicName);
//        return ResponseEntity.created(URI.create("api/alarms/" + topic)).build();
//    }

//    @GetMapping(value = "/topic/{topicName}/subscribe", produces = "text/event-stream")
//    public ResponseEntity<SseEmitter> subscribeTopic(@PathVariable String topicName, @AuthenticationPrincipal OAuth2User oAuth2User,
//                                                     @RequestHeader(value = "Last-Event_ID", required = false) String lastEventId, HttpServletResponse response) {
//        System.out.println("OAuth UserName : " + oAuth2User);
//        return new ResponseEntity<>(alarmService.subscribeTopic(topicName, "username", lastEventId, LocalDateTime.now()), HttpStatus.OK);
//    }

    @PostMapping("/topic/{topicName}")
    @ResponseStatus(HttpStatus.CREATED)
    public void pushAlarm(@PathVariable String topicName, @RequestParam(name = "sender") String sender, @RequestParam(name = "context") String context) {
        alarmService.pushAlarm(topicName, context);
    }

//    @DeleteMapping("/topic/{topicName}")
//    @ResponseStatus(HttpStatus.OK)
//    public void deleteTopic(@PathVariable String topicName) {
//        alarmService.deleteTopic(topicName);
//    }

    @GetMapping("/{memberId}/subs")
    public ResponseEntity<AlarmResponseList> getSubscribeAlarmList(@PathVariable String memberId) {
        return ResponseEntity.ok(alarmService.getSubscribeAlarmList(memberId));
    }
    @GetMapping("/{memberId}/dibs")
    public ResponseEntity<AlarmResponseList> getDibAlarmList(@PathVariable String memberId) {
        return ResponseEntity.ok(alarmService.getDibAlarmList(memberId));
    }
}
