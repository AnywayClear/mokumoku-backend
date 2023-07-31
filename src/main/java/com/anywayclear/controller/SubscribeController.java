package com.anywayclear.controller;

import com.anywayclear.dto.request.SubscribeCreateRequest;
import com.anywayclear.dto.response.SubscribeResponseList;
import com.anywayclear.service.SubscribeService;
import org.springframework.http.ResponseEntity;
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
}
