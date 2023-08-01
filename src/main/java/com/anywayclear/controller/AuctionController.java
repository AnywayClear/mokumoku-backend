package com.anywayclear.controller;

import com.anywayclear.dto.request.BiddingRequest;
import com.anywayclear.dto.response.BiddingResponse;
import com.anywayclear.service.AuctionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auctions")
//@Secured({"ROLE_CONSUMER", "ROLE_SELLER"})
public class AuctionController {
    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @PostMapping("/{auction-id}")
    public ResponseEntity<BiddingResponse> bidding(
            @PathVariable("auction-id") long auctionId,
            @RequestBody BiddingRequest request) {   // 인증정보 받기 추가 예정
        return ResponseEntity.ok(auctionService.Bidding(auctionId,request));
    }
}
