package com.anywayclear.config.oauth;

import com.anywayclear.entity.Member;
import com.anywayclear.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustumOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");

        String emailAddress = (String) kakao_account.get("email");
        String nickname = (String) profile.get("nickname");
        String image = (String) profile.get("profile_image_url");
        String id = String.valueOf(UUID.randomUUID()); // UUID Version 4를 생성하여 반환
        String userId = String.valueOf(UUID.randomUUID());

        // email로 회원가입 여부 판단
        Optional<Member> memberOptional = memberRepository.findByEmailAddress(emailAddress);
        Member member;

        if (memberOptional.isEmpty()) {
            // 회원가입
            member = Member.builder()
                    .id(id)
                    .userId(userId)
                    .emailAddress(emailAddress)
                    .image(image)
                    .nickname(nickname)
                    .role("ROLE_CUSTOMER")
                    .build();
        } else {
            member = memberOptional.get();
            member.setImage(image);
        }

        memberRepository.save(member);
        String oauth2UserRole = member.getRole();

        // Handler로 로그인된 유저객체 보내기
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(oauth2UserRole)), attributes, "id");
    }
}
