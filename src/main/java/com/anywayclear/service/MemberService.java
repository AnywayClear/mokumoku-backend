package com.anywayclear.service;

import com.anywayclear.dto.request.MemberCreateRequest;
import com.anywayclear.dto.response.MemberResponse;
import com.anywayclear.entity.Member;
import com.anywayclear.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse getMember(String id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("아이디가 없습니다."));
        return MemberResponse.toResponse(member);
    }

    public MemberResponse getMemberByUserId(String userId) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("해당 userId의 유저가 없습니다."));
        return MemberResponse.toResponse(member);
    }
}
