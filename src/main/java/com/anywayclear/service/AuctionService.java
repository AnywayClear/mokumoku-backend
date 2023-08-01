package com.anywayclear.service;

import com.anywayclear.dto.request.BiddingRequest;
import com.anywayclear.dto.response.BiddingResponse;
import com.anywayclear.repository.AuctionRepository;
import org.springframework.stereotype.Service;

@Service
public class AuctionService {
    private final AuctionRepository auctionRepository;

    public AuctionService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

//    public BiddingResponse Bidding(long auctionId,BiddingRequest request) {
//        int price = auctionRepository.findById(auctionId).orElseThrow(() -> new RuntimeException("없는 경매입니다.")).getPrice();
//
//    }
}
