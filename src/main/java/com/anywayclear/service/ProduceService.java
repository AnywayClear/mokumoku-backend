package com.anywayclear.service;

import com.anywayclear.dto.request.ProduceCreateRequest;
import com.anywayclear.dto.response.MultiResponse;
import com.anywayclear.dto.response.ProduceResponse;
import com.anywayclear.entity.Auction;
import com.anywayclear.entity.Member;
import com.anywayclear.entity.Produce;
import com.anywayclear.exception.CustomException;
import com.anywayclear.repository.AuctionRepository;
import com.anywayclear.repository.MemberRepository;
import com.anywayclear.repository.ProduceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.anywayclear.exception.ExceptionCode.INVALID_MEMBER;
import static com.anywayclear.exception.ExceptionCode.INVALID_PRODUCE_ID;

@Service
public class ProduceService {
    private final ProduceRepository produceRepository;
    private final AuctionRepository auctionRepository;
    private final MemberRepository memberRepository;
    private final AuctionService auctionService;

    public ProduceService(ProduceRepository produceRepository, AuctionRepository auctionRepository, MemberRepository memberRepository, AuctionService auctionService) {
        this.produceRepository = produceRepository;
        this.auctionRepository = auctionRepository;
        this.memberRepository = memberRepository;
        this.auctionService = auctionService;
    }

    @Transactional
    public Long createProduce(ProduceCreateRequest request, String sellerId) {
        Produce produce = produceRepository.save(Produce.toEntity(request));
        produce.setSeller(memberRepository.findByUserId(sellerId).orElseThrow(() -> new CustomException(INVALID_MEMBER)));
        for (int i = 0; i < request.getEa(); i++) {
            auctionRepository.save(new Auction(produce));
        }
        return produce.getId();
    }

    @Transactional(readOnly = true)
    public ProduceResponse getProduce(Long id) {
        Produce produce = produceRepository.findById(id).orElseThrow(() -> new CustomException(INVALID_PRODUCE_ID));
        return ProduceResponse.toResponse(produce);
    }

    @Transactional(readOnly = true)
    public MultiResponse<ProduceResponse, Produce> getProducePage(List<Integer> statusNoList, Pageable pageable, String name, String sellerId, String filter) {
        Page<Produce> producePage;
        if (filter.equals("all")) {
            producePage = produceRepository.findAllByStatusInAndNameContaining(statusNoList, pageable, name);
        } else {
            Member seller = memberRepository.findByUserId(sellerId).orElseThrow(() -> new CustomException(INVALID_MEMBER));
            producePage = produceRepository.findAllBySellerAndStatusInAndNameContaining(seller, pageable, statusNoList, name);
        }
        List<ProduceResponse> produceResponseList = producePage.map(ProduceResponse::toResponse).getContent();
        return new MultiResponse<>(produceResponseList, producePage);
    }

    @Transactional
    public void updateProduceStatus() {
        for (Produce produce : produceRepository.findByStatus(1)) {
            for (Auction auction : produce.getAuctionList()) {
                auctionService.checkAuctionFinished(auction.getId());
            }
        }
    }
}
