package com.anywayclear.controller;

import com.anywayclear.dto.request.SubscribeCreateRequest;
import com.anywayclear.dto.response.IsSubResponse;
import com.anywayclear.dto.response.SubscribeResponseList;
import com.anywayclear.service.SubscribeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/subscribes")
public class SubscribeController {
    private final SubscribeService subscribeService;

    public SubscribeController(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

    @GetMapping(value = "/{userId}/subscribe", produces = "text/event-stream")
    public ResponseEntity<SseEmitter> createSubscribe(@PathVariable String userId, @AuthenticationPrincipal OAuth2User oAuth2User,
                                                      @RequestHeader(value = "Last-Event_ID", required = false) String lastEventId, HttpServletResponse response) {
        return new ResponseEntity<>(subscribeService.createSubscribe(userId, oAuth2User, lastEventId, LocalDateTime.now()), HttpStatus.OK);
//        return ResponseEntity.created(URI.create("api/subscribes/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<SubscribeResponseList> getSubscribeList(@RequestParam(name = "userId") String userId) {
        return ResponseEntity.ok(subscribeService.getSubscribeList(userId));
    }

    @GetMapping("/{seller-id}/member")
    public ResponseEntity<IsSubResponse> getIsSub(@AuthenticationPrincipal OAuth2User oAuth2User, @PathVariable("seller-id") String sellerId) {
        String consumerId = (String) oAuth2User.getAttributes().get("userId");
        return ResponseEntity.ok(subscribeService.getIsSub(consumerId,sellerId));
    }
}
