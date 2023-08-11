package com.anywayclear.service;

import com.anywayclear.dto.request.BiddingRequest;
import com.anywayclear.dto.request.DealCreateRequest;
import com.anywayclear.dto.response.AuctionResponseList;
import com.anywayclear.dto.response.BiddingResponse;
import com.anywayclear.entity.Auction;
import com.anywayclear.entity.Member;
import com.anywayclear.entity.Produce;
import com.anywayclear.exception.CustomException;
import com.anywayclear.repository.AuctionRepository;
import com.anywayclear.repository.MemberRepository;
import com.anywayclear.repository.ProduceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.anywayclear.exception.ExceptionCode.*;

@Service
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final MemberRepository memberRepository;
    private final ProduceRepository produceRepository;

    private final DealService dealService;

    public AuctionService(AuctionRepository auctionRepository,
                          MemberRepository memberRepository, ProduceRepository produceRepository, DealService dealService) {
        this.auctionRepository = auctionRepository;
        this.memberRepository = memberRepository;
        this.produceRepository = produceRepository;
        this.dealService = dealService;
    }

    @Transactional
    public BiddingResponse Bidding(long auctionId, String consumerId, BiddingRequest request) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new CustomException(INVALID_AUCTION_ID));
        if (auction.getProduce().getStatus() == 0 || auction.isClosed()) {
            throw new CustomException(INVALID_AUCTION_STATUS);
        }
        /* 테스트동안 제한 안함 */
//        if (LocalDateTime.now().isAfter(auction.getUpdatedAt().plusMinutes(5))) {
//            throw new CustomException(EXPIRED_AUCTION_TIME);
//        }
        Member consumer = memberRepository.findByUserId(consumerId).orElseThrow(() -> new CustomException(INVALID_MEMBER));
        if (request.getPrice() < auction.getPrice() + 100) { // 가격 기준 정해지면 수정할 로직
            throw new CustomException(INVALID_PRICE);
        }
        auction.setPrice(request.getPrice());   // 트랜잭션 내에서 변경시 자동 update
        auction.setNickname(consumer.getNickname());
        return BiddingResponse.builder()
                .userId(consumerId)
                .nickname(consumer.getNickname())
                .updatedAt(auction.getUpdatedAt())
                .price(request.getPrice())
                .build();
    }

    @Transactional
    public void checkAuctionFinished(long auctionId) {
        Auction auction=auctionRepository.findById(auctionId).orElseThrow(()->new CustomException(INVALID_AUCTION_ID));
        Produce produce = auction.getProduce();
        if (produce.getStatus() == 1 && !auction.isClosed() && LocalDateTime.now().isAfter(auction.getLastBidding().plusMinutes(5))) {
            auction.setClosed(true);
            produce.setEa(produce.getEa() - 1);
            // 한 상품의 모든 경매가 종료되었을 경우
            if (produce.getEa() == 0) {
                produce.setStatus(2);
            }
        }
    }

    @Transactional(readOnly = true)
    public AuctionResponseList getAuctionList(long produceId) {
        Produce produce = produceRepository.findById(produceId).orElseThrow(() -> new CustomException(INVALID_PRODUCE_ID));
        List<Auction> auctionList = auctionRepository.findAllByProduce(produce);
        return new AuctionResponseList(auctionList);
    }

    /* 요청으로 불가해서 폐기 */
    @Transactional
    public Long changeFinished(long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new CustomException(INVALID_AUCTION_ID));
        auction.setClosed(true);
        DealCreateRequest dealCreateRequest = DealCreateRequest.builder()
                .endPrice(auction.getPrice())
                .consumer(memberRepository.findByNickname(auction.getNickname()).orElseThrow(() -> new CustomException(INVALID_MEMBER)))
                .seller(auction.getProduce().getSeller())
                .produce(auction.getProduce())
                .build();
        return dealService.createDeal(dealCreateRequest);
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
