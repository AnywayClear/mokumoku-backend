package com.anywayclear.service;

import com.anywayclear.dto.request.MemberCreateRequest;
import com.anywayclear.dto.request.MemberUpdateRequest;
import com.anywayclear.dto.response.MemberResponse;
import com.anywayclear.entity.Member;
import com.anywayclear.exception.CustomException;
import com.anywayclear.exception.ExceptionCode;
import com.anywayclear.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse getMember(String id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionCode.INVALID_MEMBER));
        return MemberResponse.toResponse(member);
    }

    public MemberResponse getMemberByUserId(String userId) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ExceptionCode.INVALID_MEMBER));
        return MemberResponse.toResponse(member);
    }

    @Transactional
    public MemberResponse updateMember(String userId, MemberUpdateRequest request) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ExceptionCode.INVALID_MEMBER));

        // 요청으로 들어온 데이터로 기존 회원 정보 업데이트
        if (request.getNickname() != null) {
            member.setNickname(request.getNickname());
        }

        if (request.getImage() != null) {
            member.setImage(request.getImage());
        }

        // SELLER 전환
        if (request.getCompanyRegistrationNumber() != null) {
            member.setRole(request.getRole());
            member.setPhoneNumber(request.getPhoneNumber());
            member.setDescription(request.getDescription());
            member.setCompanyRegistrationNumber(request.getCompanyRegistrationNumber());
            member.setCompanyAddress(request.getCompanyAddress());
        }

        return MemberResponse.toResponse(memberRepository.save(member));
    }


}
