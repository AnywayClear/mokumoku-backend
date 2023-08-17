package com.anywayclear.config.jwt;

import com.anywayclear.config.JwtConfig;
import com.anywayclear.entity.Member;
import com.anywayclear.exception.ErrorResponse;
import com.anywayclear.exception.ExceptionCode;
import com.anywayclear.repository.MemberRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.persistence.EntityNotFoundException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtConfig jwtConfig;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisAuthenticatedUserTemplate;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository, JwtConfig jwtConfig, RedisTemplate<String, String> redisAuthenticatedUserTemplate) {
        super(authenticationManager);
        this.jwtConfig = jwtConfig;
        this.memberRepository = memberRepository;
        this.redisAuthenticatedUserTemplate = redisAuthenticatedUserTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("JwtAuthorizationFilter : JWT 유효성 검사");

        // 특정 경로에 대한 요청이라면 JWT 검사를 하지 않음
        if (request.getMethod().equals("GET") && request.getRequestURI().startsWith("/api/produces")) {
            if (!request.getRequestURI().startsWith("/api/produces/")) {
                chain.doFilter(request, response);
                return;
            }
        }

        // aws target group health test 접근 허용
        if (request.getMethod().equals("GET") && request.getRequestURI().startsWith("/")) {
            chain.doFilter(request, response);
            return;
        }

        String jwtHeader = request.getHeader(jwtConfig.getHeader());

        // JWT Header, Prefix 확인
        if (jwtHeader == null || !jwtHeader.startsWith(jwtConfig.getPrefix())) {
            sendJsonResponse(response, ExceptionCode.INVALID_TOKEN);
            return;
        }

        // JWT 유효성 검사
        String accessToken = request.getHeader(jwtConfig.getHeader()).replace(jwtConfig.getPrefix() + " ", "");
        try {
            String userId = JWT.require(Algorithm.HMAC512(jwtConfig.getKey())).build().verify(accessToken).getClaim("userId").asString();
            if (userId != null) {
                try {
                    Member member = memberRepository.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("해당 JWT의 member가 없습니다. userId: " + userId));
                    if (!member.isDeleted()) {
                        if (checkDuplicatedLogin(userId, accessToken, response)) return; // 중복로그인 시 예외 처리
                        saveMemberInSecurityContextHolder(member);
                        chain.doFilter(request, response);
                    } else {
                        sendJsonResponse(response, ExceptionCode.INVALID_DELETED_MEMBER); // 탈퇴한 회원 예외 처리
                    }
                } catch (EntityNotFoundException ex) {
                    sendJsonResponse(response, ExceptionCode.INVALID_USER_ID); // 해당 userId의 회원이 없을 때 예외 처리
                }
            } else {
                sendJsonResponse(response, ExceptionCode.INVALID_TOKEN); // 복호화 했지만 userId 정보가 없는 잘못된 토큰 예외 처리
            }
        } catch (TokenExpiredException ex) {
            //refreshToken 확인
            String refreshToken = getRefreshToken(accessToken);
            if (refreshToken != null) {
                // accessToken 재발급
                String userId = JWT.require(Algorithm.HMAC512(jwtConfig.getKey())).build().verify(refreshToken).getClaim("userId").asString();
                String newAccessToken = createAccessToken(userId, accessToken,refreshToken,response);
                if (newAccessToken != null) {
                    System.out.println("accessToken 재발급");
                    response.setHeader("newAccessToken", newAccessToken);
                    chain.doFilter(request, response);
                }
            } else {
                // redis에 refreshToken없으면 만료된 토큰 처리
                sendJsonResponse(response, ExceptionCode.INVALID_EXPIRED_TOKEN);
            }
        } catch (JWTDecodeException ex) {
            // JWT 디코딩 예외 처리
            sendJsonResponse(response, ExceptionCode.INVALID_TOKEN);
        }
    }

    private boolean checkDuplicatedLogin(String userId, String token, HttpServletResponse response) throws IOException {
        try {
            String tokenInRedis = redisAuthenticatedUserTemplate.opsForValue().get(userId);
            if (tokenInRedis != null && !tokenInRedis.equals(token)) { // redis에 저장된 토큰과 다른 값이면
                sendJsonResponse(response, ExceptionCode.INVALID_DUPLICATED_AUTHENTICATION); // 중복로그인 토큰 만료 처리
                return true;
            }
            return false;
        } catch (RedisConnectionFailureException ex) {
            sendJsonResponse(response, ExceptionCode.UNCONNECTED_REDIS);
            return true;
        }
    }

    private String getRefreshToken(String accessToken) {
        String refreshToken = redisAuthenticatedUserTemplate.opsForValue().get(accessToken);
        return refreshToken;
    }

    private String createAccessToken(String userId, String accessToken, String refreshToken,HttpServletResponse response) throws IOException {
        int second = Integer.parseInt(jwtConfig.getSecond());
        int minute = Integer.parseInt(jwtConfig.getMinute());
        int hour = Integer.parseInt(jwtConfig.getHour());
        try {
            Member member = memberRepository.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("해당 JWT의 member가 없습니다. userId: " + userId));
            saveMemberInSecurityContextHolder(member);

            String newAccessToken = JWT.create()
                    .withSubject("mokumokuAccess")
                    .withExpiresAt(new Date(System.currentTimeMillis() + (second * minute)))
                    .withClaim("userId", userId)
                    .withClaim("role", member.getRole())
                    .sign(Algorithm.HMAC512(jwtConfig.getKey()));

            // 기존 user 및 jwt 데이터 삭제
            redisAuthenticatedUserTemplate.delete(userId);
            redisAuthenticatedUserTemplate.delete(accessToken);

            // redis에 newAccessToken : refreshToken 저장
            redisAuthenticatedUserTemplate.opsForValue().set(userId, newAccessToken);
            redisAuthenticatedUserTemplate.opsForValue().set(newAccessToken, refreshToken);
            redisAuthenticatedUserTemplate.expire(newAccessToken, second * minute * hour, TimeUnit.MILLISECONDS);

            return newAccessToken;
        } catch (EntityNotFoundException ex) {
            sendJsonResponse(response, ExceptionCode.INVALID_USER_ID);
            return null;
        } catch (RedisConnectionFailureException ex) {
            sendJsonResponse(response, ExceptionCode.UNCONNECTED_REDIS);
            return null;
        }
    }

    private void sendJsonResponse(HttpServletResponse response, ExceptionCode exceptionCode) throws IOException {
        response.setContentType("application/json"); // JSON 형식의 데이터라고 설정
        response.setCharacterEncoding("UTF-8"); // 인코딩 설정
        response.setStatus(exceptionCode.getCode());
        ErrorResponse errorResponse = new ErrorResponse(exceptionCode, exceptionCode.getHttpStatus(), exceptionCode.getMessage());
        new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
    }

    private void saveMemberInSecurityContextHolder(Member member) {
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
