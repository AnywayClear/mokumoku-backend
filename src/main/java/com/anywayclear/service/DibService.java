package com.anywayclear.service;

import com.anywayclear.dto.request.DibCreateRequest;
import com.anywayclear.dto.response.DibResponse;
import com.anywayclear.dto.response.DibResponseList;
import com.anywayclear.entity.Dib;
import com.anywayclear.entity.Member;
import com.anywayclear.repository.DibRepository;
import com.anywayclear.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DibService {
    private final DibRepository dibRepository;

    private final MemberRepository memberRepository;

    public DibService(DibRepository dibRepository, MemberRepository memberRepository) {
        this.dibRepository = dibRepository;
        this.memberRepository = memberRepository;
    }

    public Long createDib(DibCreateRequest request) {
        return dibRepository.save(Dib.toEntity(request)).getId();
    }

    public DibResponse getDib(Long id) {
        Dib dib = dibRepository.findById(id).orElseThrow(() -> new RuntimeException("아이디가 없습니다."));
        return DibResponse.toResponse(dib);
    }

    public DibResponseList getDibList(String nickname) { // 찜 중인 농산물 리스트 반환
        Member member = memberRepository.findByNickname(nickname).orElseThrow(() -> new RuntimeException("해당 닉네임의 유저가 없습니다."));
        List<Dib> dibList = dibRepository.findAllByConsumer(member);
        return new DibResponseList(dibList);
    }
}
