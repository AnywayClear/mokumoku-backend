package com.anywayclear.controller;

import com.anywayclear.dto.request.SubscribeCreateRequest;
import com.anywayclear.dto.response.IsSubResponse;
import com.anywayclear.dto.response.SubscribeResponseList;
import com.anywayclear.service.SubscribeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/subscribes")
public class SubscribeController {
    private final SubscribeService subscribeService;

    public SubscribeController(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

    @PostMapping
    public ResponseEntity<Void> createSubscribe(@Valid @RequestBody SubscribeCreateRequest request) {
        final Long id = subscribeService.createSubscribe(request);
        return ResponseEntity.created(URI.create("api/subscribes/" + id)).build();
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
