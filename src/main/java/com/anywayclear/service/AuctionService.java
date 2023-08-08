package com.anywayclear.service;

import com.anywayclear.dto.request.BiddingRequest;
import com.anywayclear.dto.response.BiddingResponse;
import com.anywayclear.entity.Auction;
import com.anywayclear.exception.CustomException;
import com.anywayclear.repository.AuctionRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;

import static com.anywayclear.exception.ExceptionCode.INVALID_AUCTION_ID;
import static com.anywayclear.exception.ExceptionCode.INVALID_PRICE;

@Service
public class AuctionService {
    private final AuctionRepository auctionRepository;

    public AuctionService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_READ)
    public BiddingResponse Bidding(long auctionId, BiddingRequest request) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new CustomException(INVALID_AUCTION_ID));
        if (request.getPrice() < auction.getPrice() + 100) { // 가격 기준 정해지면 수정할 로직
            throw new CustomException(INVALID_PRICE);
        }
        auction.setPrice(request.getPrice());   // 트랜잭션 내에서 변경시 자동 update
        return BiddingResponse.builder()
                .userId(request.getUserId())    // 유저 아이디는 요청한 유저에게 자동으로 받기로 수정 예정
                .price(request.getPrice())
                .build();
    }

    /**
     * test용 - 자동 최소 비딩
     */
    @Transactional
    public BiddingResponse autoBidding(long auctionId, BiddingRequest request) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new CustomException(INVALID_AUCTION_ID));
        auction.setPrice(auction.getPrice() + 100);
        return BiddingResponse.builder()
                .userId(request.getUserId())
                .price(request.getPrice())
                .build();
    }
}
