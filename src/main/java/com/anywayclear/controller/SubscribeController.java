package com.anywayclear.controller;

import com.anywayclear.dto.response.SubscribeResponseList;
import com.anywayclear.service.SubscribeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscribes")
public class SubscribeController {
    private final SubscribeService subscribeService;

    public SubscribeController(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

    @GetMapping
    public ResponseEntity<SubscribeResponseList> getSubscribeList(@RequestParam(name = "nickname") String nickname) {
        return ResponseEntity.ok(subscribeService.getSubscribeList(nickname));
    }
}
