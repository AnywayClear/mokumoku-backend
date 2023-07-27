package com.anywayclear.service;

import com.anywayclear.dto.request.MemberCreateRequest;
import com.anywayclear.dto.response.MemberResponse;
import com.anywayclear.entity.Member;
import com.anywayclear.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String createMember(MemberCreateRequest request) {
        return memberRepository.save(Member.toEntity(request)).getId();
    }

    public MemberResponse getMember(String id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("아이디가 없습니다."));
        return MemberResponse.toResponse(member);
    }
}
