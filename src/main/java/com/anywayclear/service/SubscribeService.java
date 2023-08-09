package com.anywayclear.service;

import com.anywayclear.dto.request.SubscribeCreateRequest;
import com.anywayclear.dto.response.IsSubResponse;
import com.anywayclear.dto.response.SubscribeResponse;
import com.anywayclear.dto.response.SubscribeResponseList;
import com.anywayclear.entity.Member;
import com.anywayclear.entity.Subscribe;
import com.anywayclear.exception.CustomException;
import com.anywayclear.repository.MemberRepository;
import com.anywayclear.repository.SubscribeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.anywayclear.exception.ExceptionCode.INVALID_MEMBER;

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
        return SubscribeResponse.toResponse(subscribe.getSeller());
    }

    public SubscribeResponseList getSubscribeList(String userId) {
        // @AuthenticationPrincipal OAuth2User oauthuser
        Member member = memberRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("해당 userId의 유저가 없습니다."));
        List<Member> memberList = new ArrayList<>();
        if (member.getRole().equals("ROLE_SELLER")) { // 판매자 일 경우
            List<Subscribe> subscribeList = subscribeRepository.findAllBySeller(member);
            for (Subscribe subscribe : subscribeList) {
                memberList.add(subscribe.getConsumer());
            }
        } else { // 소비자 일 경우
            List<Subscribe> subscribeList = subscribeRepository.findAllByConsumer(member);
            for (Subscribe subscribe : subscribeList) {
                memberList.add(subscribe.getConsumer());
            }
        }
        return new SubscribeResponseList(memberList);
    }

    public IsSubResponse getIsSub(String consumerId, String sellerId) {
        Member consumer = memberRepository.findByUserId(consumerId).orElseThrow(() -> new CustomException(INVALID_MEMBER));
        Member seller = memberRepository.findByUserId(sellerId).orElseThrow(() -> new CustomException(INVALID_MEMBER));
        boolean isSub = subscribeRepository.findByConsumerAndSeller(consumer, seller).isPresent();
        return new IsSubResponse(isSub);
    }
}
