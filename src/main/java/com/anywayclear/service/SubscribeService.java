package com.anywayclear.service;

import com.anywayclear.dto.request.SubscribeCreateRequest;
import com.anywayclear.dto.response.SubscribeResponse;
import com.anywayclear.dto.response.SubscribeResponseList;
import com.anywayclear.entity.Member;
import com.anywayclear.entity.Subscribe;
import com.anywayclear.repository.MemberRepository;
import com.anywayclear.repository.SubscribeRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscribeService {
    private final SubscribeRepository subscribeRepository;
    private final MemberRepository memberRepository;

    public SubscribeService(SubscribeRepository subscribeRepository, MemberRepository memberRepository) {
        this.subscribeRepository = subscribeRepository;
        this.memberRepository = memberRepository;
    }

    public Long createSubscribe(SubscribeCreateRequest request) {
        Member consumer = memberRepository.findByUserId(request.getConsumerId()).orElseThrow(() -> new RuntimeException("아이디가 없습니다."));
        Member seller = memberRepository.findByUserId(request.getSellerId()).orElseThrow(() -> new RuntimeException("아이디가 없습니다."));
        Subscribe subscribe = new Subscribe(consumer, seller);
        return subscribeRepository.save(subscribe).getId();
    }

    public SubscribeResponse getSubscribe(Long id) {
        Subscribe subscribe = subscribeRepository.findById(id).orElseThrow(() -> new RuntimeException());
        return SubscribeResponse.toResponse(subscribe);
    }

    public SubscribeResponseList getSubscribeList(String userId) {
        // @AuthenticationPrincipal OAuth2User oauthuser
        Member member = memberRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("해당 userId의 유저가 없습니다."));
        System.out.println("--------member--------");
        System.out.println(member);
        List<Subscribe> subscribeList;
        if (member.getRole().equals("ROLE_SELLER")) { // 판매자 일 경우
            subscribeList = subscribeRepository.findAllBySeller(member);
        } else { // 소비자 일 경우
            subscribeList = subscribeRepository.findAllByConsumer(member);
        }
        return new SubscribeResponseList(subscribeList);
    }
}
