package com.anywayclear.controller;

import com.anywayclear.dto.request.BiddingRequest;
import com.anywayclear.dto.response.BiddingResponse;
import com.anywayclear.service.AuctionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auctions")
public class AuctionController {
    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @PostMapping("/{auction-id}")
    public ResponseEntity<BiddingResponse> bidding(
            @AuthenticationPrincipal OAuth2User oAuth2User,
            @PathVariable("auction-id") long auctionId,
            @RequestBody BiddingRequest request) {   // 인증정보 받기 추가 예정
        String consumerId = (String) oAuth2User.getAttributes().get("userId");
        auctionService.checkAuctionFinished(auctionId);
        return ResponseEntity.ok(auctionService.Bidding(auctionId, consumerId, request));
    }
}
