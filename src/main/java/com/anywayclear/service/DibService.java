package com.anywayclear.service;

import com.anywayclear.dto.response.DibResponse;
import com.anywayclear.dto.response.IsDibResponse;
import com.anywayclear.dto.response.MultiResponse;
import com.anywayclear.entity.Dib;
import com.anywayclear.entity.Member;
import com.anywayclear.entity.Produce;
import com.anywayclear.exception.CustomException;
import com.anywayclear.repository.DibRepository;
import com.anywayclear.repository.MemberRepository;
import com.anywayclear.repository.ProduceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;

import static com.anywayclear.exception.ExceptionCode.*;

@Service
public class DibService {
    private final DibRepository dibRepository;

    private final MemberRepository memberRepository;
    private final ProduceRepository produceRepository;

    private final AlarmService alarmService;

    public DibService(DibRepository dibRepository, MemberRepository memberRepository, ProduceRepository produceRepository, AlarmService alarmService) {
        this.dibRepository = dibRepository;
        this.memberRepository = memberRepository;
        this.produceRepository = produceRepository;
        this.alarmService = alarmService;
    }

    @Transactional
    public SseEmitter createDib(Long topicName, OAuth2User oAuth2User, String lastEventId, LocalDateTime now) {
        String userId = (String) oAuth2User.getAttributes().get("userId");

        Member member = memberRepository.findByUserId(userId).orElseThrow(() -> new CustomException(INVALID_MEMBER));
        Produce produce = produceRepository.findById(topicName).orElseThrow(() -> new CustomException(INVALID_PRODUCE_ID));
        Dib dib = new Dib(member, produce);
        dibRepository.save(dib);

        return alarmService.createEmitter(topicName.toString(), userId, lastEventId, now);
    }

    @Transactional(readOnly = true)
    public DibResponse getDib(Long id) {
        Dib dib = dibRepository.findById(id).orElseThrow(() -> new CustomException(INVALID_DIB_ID));
        return DibResponse.toResponse(dib);
    }

    @Transactional(readOnly = true)
    public MultiResponse<DibResponse, Dib> getDibPage(String userId, Pageable pageable) { // 찜 중인 농산물 리스트 반환
        Member member = memberRepository.findByUserId(userId).orElseThrow(() -> new CustomException(INVALID_MEMBER));
        Page<Dib> dibPage = dibRepository.findAllByConsumer(member, pageable);
        List<DibResponse> dibResponseList = dibPage.map(DibResponse::toResponse).getContent();
        return new MultiResponse<>(dibResponseList, dibPage);
    }

    @Transactional(readOnly = true)
    public IsDibResponse getIsDib(String userId, long produceId) {
        Produce produce = produceRepository.findById(produceId).orElseThrow(() -> new CustomException(INVALID_PRODUCE_ID));
        Member consumer = memberRepository.findByUserId(userId).orElseThrow(() -> new CustomException(INVALID_MEMBER));
        boolean isDib = dibRepository.findByConsumerAndProduce(consumer, produce).isPresent();
        return new IsDibResponse(isDib);
    }

    @Transactional
    public void deleteDib(Long produceId, String consumerId) {
        Member consumer = memberRepository.findByUserId(consumerId).orElseThrow(() -> new CustomException(INVALID_MEMBER));
        Produce produce = produceRepository.findById(produceId).orElseThrow(() -> new CustomException(INVALID_MEMBER));
        dibRepository.deleteByConsumerAndProduce(consumer, produce);
    }
}
