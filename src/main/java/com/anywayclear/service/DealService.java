package com.anywayclear.service;

import com.anywayclear.dto.request.DealCreateRequest;
import com.anywayclear.dto.response.DealResponse;
import com.anywayclear.dto.response.DealResponseList;
import com.anywayclear.entity.Deal;
import com.anywayclear.entity.Member;
import com.anywayclear.repository.DealRepository;
import com.anywayclear.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DealService {
    private final DealRepository dealRepository;
    private final MemberRepository memberRepository;

    public DealService(DealRepository dealRepository, MemberRepository memberRepository) {
        this.dealRepository = dealRepository;
        this.memberRepository = memberRepository;
    }

    public Long createDeal(DealCreateRequest request) {
        return dealRepository.save(Deal.toEntity(request)).getId();
    }

    public DealResponse getDeal(Long id) {
        Deal deal = dealRepository.findById(id).orElseThrow(() -> new RuntimeException());
        return DealResponse.toResponse(deal);
    }

    public DealResponseList getDealList(String userId) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("해당 userId의 유저가 없습니다."));
        List<Deal> dealList;
        if (member.getRole() == "ROLE_SELLER") { // 판매자 일 경우
            dealList = dealRepository.findAllBySeller(member);
        } else { // 소비자 일 경우
            dealList = dealRepository.findAllByConsumer(member);
        }
        return new DealResponseList(dealList);
    }
}
