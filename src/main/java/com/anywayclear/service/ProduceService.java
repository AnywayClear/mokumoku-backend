package com.anywayclear.service;

import com.anywayclear.dto.request.ProduceCreateRequest;
import com.anywayclear.dto.response.MultiResponse;
import com.anywayclear.dto.response.ProduceResponse;
import com.anywayclear.entity.Auction;
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

    public ProduceService(ProduceRepository produceRepository, AuctionRepository auctionRepository, MemberRepository memberRepository) {
        this.produceRepository = produceRepository;
        this.auctionRepository = auctionRepository;
        this.memberRepository = memberRepository;
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
    public MultiResponse<ProduceResponse,Produce> getProducePage(List<Integer> statusNoList, Pageable pageable) {
        Page<Produce> producePage = produceRepository.findAllByStatusIn(statusNoList,pageable);
        List<ProduceResponse> produceResponseList = producePage.map(ProduceResponse::toResponse).getContent();
        return new MultiResponse<>(produceResponseList,producePage);
    }
}
