package com.anywayclear.config.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.Jwts;

import static com.anywayclear.config.jwt.JwtProperties.*;

@Component
public class JwtProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
    private Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // 인증 정보를 기반으로 JWT 토큰 생성하는 메서드
    public String createToken(Authentication authentication) {
        System.out.println(">>>>>>>>>>>> ");

        // 사용자 정보 가져오기
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("Provider ================== ");
        System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes());

        // 현재 시간과 토큰 만료 시간 설정
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        // JWT 토큰 생성
        return Jwts.builder()
                .setSubject((String) oAuth2User.getAttributes().get("userId")) // userId를 토큰의 subject로 설정
                .setIssuedAt(new Date()) // 토큰 발행 시간 설정
                .setExpiration(expiryDate) // 토큰 만료 시간 설정
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512) // 토큰 서명에 사용할 비밀키 설정
                .compact(); // 최종적으로 JWT 토큰 문자열로 변환
    }
}
