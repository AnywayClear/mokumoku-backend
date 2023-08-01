package com.anywayclear.controller;

import com.anywayclear.dto.request.BiddingRequest;
import com.anywayclear.dto.response.BiddingResponse;
import com.anywayclear.service.AuctionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/auctions")
//@Secured({"ROLE_CONSUMER", "ROLE_SELLER"})
public class AuctionController {
    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

//    @PostMapping("/{auction-id}")
//    public ResponseEntity<BiddingResponse> bidding(@PathVariable("auction-id") long auctionId, @Valid @RequestBody BiddingRequest request) {
//        return auctionService.Bidding(auctionId,request);
//    }
}
