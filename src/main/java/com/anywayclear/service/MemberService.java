package com.anywayclear.service;

import com.anywayclear.dto.request.MemberCreateRequest;
import com.anywayclear.dto.response.MemberResponse;
import com.anywayclear.entity.Member;
import com.anywayclear.repository.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class MemberService extends DefaultOAuth2UserService{

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String createMember(MemberCreateRequest request) {
        return memberRepository.save(Member.toEntity(request)).getId();
    }

    public MemberResponse getMember(String id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("아이디가 없습니다."));

        // 회원가입

        return MemberResponse.toResponse(member);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("user : " + super.loadUser(userRequest).getAttributes()); //카카오에서 받은 user 정보

        return super.loadUser(userRequest);
    }
}
