package com.anywayclear.service;

import com.anywayclear.dto.request.BiddingRequest;
import com.anywayclear.dto.response.BiddingResponse;
import com.anywayclear.entity.Auction;
import com.anywayclear.entity.Member;
import com.anywayclear.exception.CustomException;
import com.anywayclear.repository.AuctionRepository;
import com.anywayclear.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.anywayclear.exception.ExceptionCode.*;

@Service
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final MemberRepository memberRepository;

    public AuctionService(AuctionRepository auctionRepository,
                          MemberRepository memberRepository) {
        this.auctionRepository = auctionRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public BiddingResponse Bidding(long auctionId, String consumerId,BiddingRequest request) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new CustomException(INVALID_AUCTION_ID));
        Member consumer = memberRepository.findByUserId(consumerId).orElseThrow(() -> new CustomException(INVALID_MEMBER));
        if (request.getPrice() < auction.getPrice() + 100) { // 가격 기준 정해지면 수정할 로직
            throw new CustomException(INVALID_PRICE);
        }
        auction.setPrice(request.getPrice());   // 트랜잭션 내에서 변경시 자동 update
        return BiddingResponse.builder()
                .userId(consumerId)
                .nickname(consumer.getNickname())
                .updatedAt(auction.getUpdatedAt())
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
                .price(request.getPrice())
                .build();
    }
}
