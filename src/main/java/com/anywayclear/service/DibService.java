package com.anywayclear.service;

import com.anywayclear.dto.request.DibCreateRequest;
import com.anywayclear.dto.response.DibResponse;
import com.anywayclear.dto.response.DibResponseList;
import com.anywayclear.entity.Dib;
import com.anywayclear.entity.Member;
import com.anywayclear.entity.Produce;
import com.anywayclear.repository.DibRepository;
import com.anywayclear.repository.MemberRepository;
import com.anywayclear.repository.ProduceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return DibResponse.toResponse(dib);
    }

    public DibResponseList getDibList(String userId) { // 찜 중인 농산물 리스트 반환
        Member member = memberRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("해당 userId의 유저가 없습니다."));
        List<Dib> dibList = dibRepository.findAllByConsumer(member);
        return new DibResponseList(dibList);
    }
}
