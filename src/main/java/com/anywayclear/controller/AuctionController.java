package com.anywayclear.controller;

import com.anywayclear.dto.request.BiddingRequest;
import com.anywayclear.dto.response.BiddingResponse;
import com.anywayclear.service.AuctionService;
import org.springframework.http.ResponseEntity;
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
    public synchronized ResponseEntity<BiddingResponse> bidding(
            @PathVariable("auction-id") long auctionId,
            @RequestBody BiddingRequest request) {   // 인증정보 받기 추가 예정
        return ResponseEntity.ok(auctionService.Bidding(auctionId, request));
    }

    /*
    * 테스트용
    */
    @PostMapping("/{auction-id}/test")
    public ResponseEntity<BiddingResponse> test(
            @PathVariable("auction-id") long auctionId,
            @RequestBody BiddingRequest request){
        return ResponseEntity.ok(auctionService.autoBidding(auctionId, request));
    }
}
