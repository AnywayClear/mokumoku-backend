package com.anywayclear.service;

import com.anywayclear.dto.request.BiddingRequest;
import com.anywayclear.dto.request.DealCreateRequest;
import com.anywayclear.dto.response.AuctionResponse;
import com.anywayclear.dto.response.AuctionResponseList;
import com.anywayclear.dto.response.BiddingResponse;
import com.anywayclear.dto.response.ProduceResponse;
import com.anywayclear.entity.Auction;
import com.anywayclear.entity.Member;
import com.anywayclear.entity.Produce;
import com.anywayclear.exception.CustomException;
import com.anywayclear.repository.AuctionRepository;
import com.anywayclear.repository.MemberRepository;
import com.anywayclear.repository.ProduceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.anywayclear.exception.ExceptionCode.*;

@Service
@Slf4j
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final MemberRepository memberRepository;
    private final ProduceRepository produceRepository;
    private final DealService dealService;
    private final NotificationService notificationService;

    public AuctionService(AuctionRepository auctionRepository, MemberRepository memberRepository, ProduceRepository produceRepository, DealService dealService, NotificationService notificationService) {
        this.auctionRepository = auctionRepository;
        this.memberRepository = memberRepository;
        this.produceRepository = produceRepository;
        this.dealService = dealService;
        this.notificationService = notificationService;
    }

    @Transactional
    public BiddingResponse Bidding(long auctionId, String consumerId, BiddingRequest request) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new CustomException(INVALID_AUCTION_ID));
        if (auction.getProduce().getStatus() == 0 || auction.isClosed()) {
            throw new CustomException(INVALID_AUCTION_STATUS);
        }
        /* 테스트동안 제한 안함 */
        // 테스트용으로 1분
        if (LocalDateTime.now().isAfter(auction.getLastBidding().plusMinutes(1))) {
            throw new CustomException(EXPIRED_AUCTION_TIME);
        }
        Member consumer = memberRepository.findByUserId(consumerId).orElseThrow(() -> new CustomException(INVALID_MEMBER));
        if (request.getPrice() < auction.getPrice() + 100) { // 가격 기준 정해지면 수정할 로직
            throw new CustomException(INVALID_PRICE);
        }
        auction.setPrice(request.getPrice());   // 트랜잭션 내에서 변경시 자동 update
        auction.setNickname(consumer.getNickname());
        auction.setLastBidding(LocalDateTime.now());
        return BiddingResponse.builder().userId(consumerId).nickname(consumer.getNickname()).updatedAt(auction.getLastBidding()).price(request.getPrice()).build();
    }

    @Transactional
    public void checkAuctionFinished(long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new CustomException(INVALID_AUCTION_ID));
        Produce produce = auction.getProduce();
        // 테스트용으로 1분
        log.debug(AuctionResponse.toResponse(auction).toString());
        log.debug(ProduceResponse.toResponse(produce).toString());
        if (produce.getStatus() == 1 && !auction.isClosed() && LocalDateTime.now().isAfter(auction.getLastBidding().plusMinutes(1))) {
            auction.setClosed(true);
            produce.setEndDate(auction.getLastBidding());
            if (!produce.getStartDate().equals(auction.getLastBidding())) {
                Member consumer = memberRepository.findByNickname(auction.getNickname()).orElseThrow(() -> new CustomException(INVALID_MEMBER));
                DealCreateRequest dealCreateRequest = DealCreateRequest.builder()
                        .endPrice(auction.getPrice())
                        .produce(produce)
                        .seller(produce.getSeller())
                        .consumer(consumer)
                        .build();
                dealService.createDeal(dealCreateRequest);
                notificationService.notify(consumer.getUserId(), auction);
            }
            produce.setStatus(2);
            for (Auction a : produce.getAuctionList()) {
                if (!a.isClosed()) {
                    produce.setStatus(1);
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public AuctionResponseList getAuctionList(long produceId) {
        Produce produce = produceRepository.findById(produceId).orElseThrow(() -> new CustomException(INVALID_PRODUCE_ID));
        List<Auction> auctionList = auctionRepository.findAllByProduce(produce);
        return new AuctionResponseList(auctionList);
    }
}
