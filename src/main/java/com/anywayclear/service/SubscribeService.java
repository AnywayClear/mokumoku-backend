package com.anywayclear.service;

import com.anywayclear.dto.response.IsSubResponse;
import com.anywayclear.dto.response.SubscribeResponse;
import com.anywayclear.dto.response.SubscribeResponseList;
import com.anywayclear.entity.Member;
import com.anywayclear.entity.Subscribe;
import com.anywayclear.exception.CustomException;
import com.anywayclear.repository.MemberRepository;
import com.anywayclear.repository.SubscribeRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.anywayclear.exception.ExceptionCode.INVALID_MEMBER;
import static com.anywayclear.exception.ExceptionCode.INVALID_SUBSCRIBE_ID;

@Service
public class SubscribeService {
    private final SubscribeRepository subscribeRepository;
    private final MemberRepository memberRepository;

    private AlarmService alarmService;

    public SubscribeService(SubscribeRepository subscribeRepository, MemberRepository memberRepository, AlarmService alarmService) {
        this.subscribeRepository = subscribeRepository;
        this.memberRepository = memberRepository;
        this.alarmService = alarmService;
    }

    @Transactional
    public Long createSubscribe(String topicName, OAuth2User oAuth2User) {
        String userId = (String) oAuth2User.getAttributes().get("userId");

        Member consumer = memberRepository.findByUserId(userId).orElseThrow(() -> new CustomException(INVALID_MEMBER));
        Member seller = memberRepository.findByUserId(topicName).orElseThrow(() -> new CustomException(INVALID_MEMBER));
        Subscribe subscribe = new Subscribe(consumer, seller);
        subscribeRepository.save(subscribe);
        return subscribe.getId();
    }

    @Transactional
    public SubscribeResponse getSubscribe(Long id) {
        Subscribe subscribe = subscribeRepository.findById(id).orElseThrow(() -> new CustomException(INVALID_SUBSCRIBE_ID));
        return SubscribeResponse.toResponse(subscribe.getSeller());
    }

    @Transactional
    public SubscribeResponseList getSubscribeList(String userId) {
        // @AuthenticationPrincipal OAuth2User oauthuser
        Member member = memberRepository.findByUserId(userId).orElseThrow(() -> new CustomException(INVALID_MEMBER));
        List<Member> memberList = new ArrayList<>();
        if (member.getRole().equals("ROLE_SELLER")) { // 판매자 일 경우
            List<Subscribe> subscribeList = subscribeRepository.findAllBySeller(member);
            for (Subscribe subscribe : subscribeList) {
                memberList.add(subscribe.getConsumer());
            }
        } else { // 소비자 일 경우
            List<Subscribe> subscribeList = subscribeRepository.findAllByConsumer(member);
            for (Subscribe subscribe : subscribeList) {
                memberList.add(subscribe.getSeller());
            }
        }
        return new SubscribeResponseList(memberList);
    }

    @Transactional
    public IsSubResponse getIsSub(String consumerId, String sellerId) {
        Member consumer = memberRepository.findByUserId(consumerId).orElseThrow(() -> new CustomException(INVALID_MEMBER));
        Member seller = memberRepository.findByUserId(sellerId).orElseThrow(() -> new CustomException(INVALID_MEMBER));
        boolean isSub = subscribeRepository.findByConsumerAndSeller(consumer, seller).isPresent();
        return new IsSubResponse(isSub);
    }

    @Transactional
    public void deleteSubscribe(String sellerId, String consumerId) {
        Member consumer = memberRepository.findByUserId(consumerId).orElseThrow(() -> new CustomException(INVALID_MEMBER));
        Member seller = memberRepository.findByUserId(sellerId).orElseThrow(() -> new CustomException(INVALID_MEMBER));
        subscribeRepository.deleteByConsumerAndSeller(consumer, seller);
    }
}
