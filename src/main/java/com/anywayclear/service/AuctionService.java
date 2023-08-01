package com.anywayclear.service;

import com.anywayclear.dto.request.BiddingRequest;
import com.anywayclear.dto.response.BiddingResponse;
import com.anywayclear.entity.Auction;
import com.anywayclear.repository.AuctionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuctionService {
    private final AuctionRepository auctionRepository;

    public AuctionService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    @Transactional
    public BiddingResponse Bidding(long auctionId,BiddingRequest request) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new RuntimeException("없는 경매입니다."));
        if (request.getPrice() < auction.getPrice() + 100) { // 가격 기준 정해지면 수정할 로직
            throw new RuntimeException("최저입찰보다 낮은 가격입니다!");
        }
        auction.setPrice(request.getPrice());
        return BiddingResponse.builder()
                .userId(request.getUserId())    // 유저 아이디는 요청한 유저에게 자동으로 받기로 수정 예정
                .price(request.getPrice())
                .build();
    }
}
