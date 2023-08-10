package com.anywayclear.controller;

import com.anywayclear.dto.request.DealCreateRequest;
import com.anywayclear.dto.response.DealResponse;
import com.anywayclear.dto.response.MultiResponse;
import com.anywayclear.entity.Deal;
import com.anywayclear.service.DealService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/deals")
public class DealController {
    private final DealService dealService;

    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    @PostMapping
    public ResponseEntity<Void> createDeal(@Valid @RequestBody DealCreateRequest request) {
        final Long id = dealService.createDeal(request);
        return ResponseEntity.created(URI.create("/api/deals/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<MultiResponse<DealResponse, Deal>> getDealList(@AuthenticationPrincipal OAuth2User oAuth2User, Pageable pageable) {
        String userId = (String) oAuth2User.getAttributes().get("userId");
        return ResponseEntity.ok(dealService.getDealList(userId, pageable));
    }
}
