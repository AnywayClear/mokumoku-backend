package com.anywayclear.service;

import com.anywayclear.dto.request.ProduceCreateRequest;
import com.anywayclear.dto.response.ProduceResponse;
import com.anywayclear.dto.response.ProduceResponseList;
import com.anywayclear.entity.Auction;
import com.anywayclear.entity.Produce;
import com.anywayclear.exception.CustomException;
import com.anywayclear.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.anywayclear.exception.ExceptionCode.INVALID_MEMBER;
import static com.anywayclear.exception.ExceptionCode.INVALID_PRODUCE_ID;

@Service
public class ProduceService {
    private final ProduceRepository produceRepository;
    private final AuctionRepository auctionRepository;
    private final DibRepository dibRepository;
    private final SubscribeRepository subscribeRepository;
    private final MemberRepository memberRepository;

    public ProduceService(ProduceRepository produceRepository, AuctionRepository auctionRepository, DibRepository dibRepository, SubscribeRepository subscribeRepository, MemberRepository memberRepository) {
        this.produceRepository = produceRepository;
        this.auctionRepository = auctionRepository;
        this.dibRepository = dibRepository;
        this.subscribeRepository = subscribeRepository;
        this.memberRepository = memberRepository;
    }

    public Long createProduce(ProduceCreateRequest request, String sellerId) {
        Produce produce = produceRepository.save(Produce.toEntity(request));
        produce.setSeller(memberRepository.findByUserId(sellerId).orElseThrow(() -> new CustomException(INVALID_MEMBER)));
        for (int i = 0; i < request.getEa(); i++) {
            auctionRepository.save(new Auction(produce));
        }
        return produce.getId();
    }

    @Transactional(readOnly = true)
    public ProduceResponse getProduce(Long id, String userId) {
        Produce produce = produceRepository.findById(id).orElseThrow(() -> new CustomException(INVALID_PRODUCE_ID));
        return ProduceResponse.toResponse(produce);
    }

    @Transactional(readOnly = true)
    public ProduceResponseList getProduceList(List<Integer> statusNoList) {
        List<Produce> produceList = produceRepository.findByStatusIn(statusNoList);
        return new ProduceResponseList(produceList);
    }
}
