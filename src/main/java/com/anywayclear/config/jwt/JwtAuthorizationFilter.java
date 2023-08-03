package com.anywayclear.config.jwt;

import com.anywayclear.config.JwtConfig;
import com.anywayclear.entity.Member;
import com.anywayclear.repository.MemberRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtConfig jwtConfig;
    private final MemberRepository memberRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository, JwtConfig jwtConfig) {
        super(authenticationManager);
        this.jwtConfig = jwtConfig;
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("JwtAuthorizationFilter : 인증이나 권한이 필요한 주소 요청이 됨");

        String jwtHeader = request.getHeader(jwtConfig.getHeader());
        System.out.println("jwtHeader = " + jwtHeader);

        // JWT 토큰을 검증을 해서 정상적인 사용자인지 확인
        if (jwtHeader == null || !jwtHeader.startsWith(jwtConfig.getPrefix())) {
            chain.doFilter(request, response);   // 다시 필터 타게 넘김
            return;
        }

        try {
            // JWT 토큰을 검증해서 정상적인 사용자인지 확인
            String accessToken = request.getHeader(jwtConfig.getHeader()).replace(jwtConfig.getPrefix()+ " ","");
            String userId = JWT.require(Algorithm.HMAC512(jwtConfig.getKey())).build().verify(accessToken).getClaim("userId").asString();
            // 서명이 정상적으로 됨
            if (userId != null) {
                Optional<Member> memberOptional = memberRepository.findByUserId(userId);
                memberOptional.ifPresent(this::processValidJwt);

                System.out.println("인가 완료");
            }
            chain.doFilter(request,response);
        } catch (TokenExpiredException ex) {
            // 만료된 토큰 처리
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            response.getWriter().write("Token has expired");
        } catch (JWTDecodeException ex) {
            // JWT 디코딩 예외 처리
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
            response.getWriter().write("Invalid JWT Token");
        }
    }

    private void processValidJwt(Member member) {
        Map<String, Object> userAttributes = createNewAttribute(member);
        DefaultOAuth2User oAuth2User = new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole())),
                userAttributes, "id"
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                oAuth2User, null, oAuth2User.getAuthorities()
        );
        storeAuthenticationInSecurityContext(authentication);
    }

    private void storeAuthenticationInSecurityContext(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Map<String, Object> createNewAttribute(Member member) {
        Map<String, Object> newAttributes = new HashMap<>();
        newAttributes.put("id", member.getId());
        newAttributes.put("userId", member.getUserId());
        newAttributes.put("role", member.getRole());
        return newAttributes;
    }
}
