package com.anywayclear.config.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustumOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Member member = getOrCreateMember(oAuth2User);

        Map<String, Object> userAttributes = createNewAttribute(member);

        // Spring Security의 세션에 OAuth2User객체 저장됨
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(member.getRole())), userAttributes, "id");
    }

    private Member getOrCreateMember(OAuth2User oAuth2User) {
        Map<String, Object> kakao_account = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
        String emailAddress = (String) kakao_account.get("email");

        Optional<Member> memberOptional = memberRepository.findByEmailAddress(emailAddress);

        if (memberOptional.isPresent()) {
            return memberOptional.get();
        } else {
            Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");
            String nickname = (String) profile.get("nickname");
            String image = (String) profile.get("profile_image_url");

            return createMember(emailAddress, nickname, image);
        }
    }

    private Member createMember(String emailAddress, String nickname, String image) {
        String id = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        Member member = Member.builder()
                .id(id)
                .userId(userId)
                .emailAddress(emailAddress)
                .image(image)
                .nickname(nickname)
                .role("ROLE_CUSTOMER")
                .build();

        return memberRepository.save(member);
    }

    private Map<String, Object> createNewAttribute(Member member) {
        Map<String, Object> newAttributes = new HashMap<>();
        newAttributes.put("id", member.getId());
        newAttributes.put("userId", member.getUserId());
        newAttributes.put("role", member.getRole());
        return newAttributes;
    }
}
