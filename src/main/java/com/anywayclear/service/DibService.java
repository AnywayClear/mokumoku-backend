package com.anywayclear.service;

import com.anywayclear.dto.request.DibCreateRequest;
import com.anywayclear.dto.response.DibResponse;
import com.anywayclear.dto.response.DibResponseList;
import com.anywayclear.dto.response.IsDibResponse;
import com.anywayclear.entity.Dib;
import com.anywayclear.entity.Member;
import com.anywayclear.entity.Produce;
import com.anywayclear.exception.CustomException;
import com.anywayclear.repository.DibRepository;
import com.anywayclear.repository.MemberRepository;
import com.anywayclear.repository.ProduceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.anywayclear.exception.ExceptionCode.INVALID_MEMBER;
import static com.anywayclear.exception.ExceptionCode.INVALID_PRODUCE_ID;

@Service
public class DibService {
    private final DibRepository dibRepository;

    private final MemberRepository memberRepository;
    private final ProduceRepository produceRepository;

    public DibService(DibRepository dibRepository, MemberRepository memberRepository, ProduceRepository produceRepository) {
        this.dibRepository = dibRepository;
        this.memberRepository = memberRepository;
        this.produceRepository = produceRepository;
    }

    public Long createDib(DibCreateRequest request) {
        Member member = memberRepository.findByUserId(request.getConsumerId()).orElseThrow(() -> new RuntimeException("아이디가 없습니다."));
        Produce produce = produceRepository.findById(request.getProduceId()).orElseThrow(() -> new RuntimeException("해당 농산물이 없습니다."));
        Dib dib = new Dib(member, produce);
        return dibRepository.save(dib).getId();
    }

    public DibResponse getDib(Long id) {
        Dib dib = dibRepository.findById(id).orElseThrow(() -> new RuntimeException("아이디가 없습니다."));
        return DibResponse.toResponse(dib.getProduce());
    }

    public DibResponseList getDibList(String userId, Pageable pageable) { // 찜 중인 농산물 리스트 반환
        Member member = memberRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("해당 userId의 유저가 없습니다."));
        List<Produce> dibList = dibRepository.findAllByConsumer(member)
                .stream()
                .map(Dib::getProduce)
                .collect(Collectors.toList());
        return new DibResponseList(dibList, pageable);
    }

    public IsDibResponse getIsDib(String userId, long produceId) {
        Produce produce = produceRepository.findById(produceId).orElseThrow(() -> new CustomException(INVALID_PRODUCE_ID));
        Member consumer = memberRepository.findByUserId(userId).orElseThrow(() -> new CustomException(INVALID_MEMBER));
        boolean isDib = dibRepository.findByConsumerAndProduce(consumer, produce).isPresent();
        return new IsDibResponse(isDib);
    }
}
