package com.anywayclear.service;

import com.anywayclear.dto.request.DealCreateRequest;
import com.anywayclear.dto.response.DealResponse;
import com.anywayclear.dto.response.MultiResponse;
import com.anywayclear.entity.Deal;
import com.anywayclear.entity.Member;
import com.anywayclear.exception.CustomException;
import com.anywayclear.repository.DealRepository;
import com.anywayclear.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.anywayclear.exception.ExceptionCode.INVALID_DEAL;
import static com.anywayclear.exception.ExceptionCode.INVALID_MEMBER;

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
        Deal deal = dealRepository.findById(id).orElseThrow(() -> new CustomException(INVALID_DEAL));
        return DealResponse.toResponse(deal);
    }

    @Transactional(readOnly = true)
    public MultiResponse<DealResponse, Deal> getDealList(String userId, Pageable pageable) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(() -> new CustomException(INVALID_MEMBER));
        Page<Deal> dealPage;
        if (member.getRole().equals("ROLE_SELLER")) { // 판매자 일 경우
            dealPage = dealRepository.findAllBySeller(member, pageable);
        } else { // 소비자 일 경우
            dealPage = dealRepository.findAllByConsumer(member, pageable);
        }
        List<DealResponse> dealResponseList = dealPage.map(DealResponse::toResponse).getContent();
        return new MultiResponse<>(dealResponseList, dealPage);
    }
}
