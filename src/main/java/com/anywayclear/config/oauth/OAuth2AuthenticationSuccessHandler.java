package com.anywayclear.config.oauth;

import com.anywayclear.config.JwtConfig;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Log4j2
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtConfig jwtConfig;
    private final RedisTemplate<String, String> redisAuthenticatedUserTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        System.out.println("OAuth2AuthenticationSuccessHandler : 로그인 성공");

        // 이미 응답이 커밋됐는데 response하면 예외 발생하므로 return
        if (response.isCommitted()) {
            log.debug("Response has already been committed");
            return;
        }

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String userId = (String) oAuth2User.getAttributes().get("userId");
        String role = (String) oAuth2User.getAttributes().get("role");
        int[] expirationTimes = setExpirationTime();
        int accessExpirationTime = expirationTimes[0];
        int refreshExpirationTime = expirationTimes[1];

        // token 생성
        String[] tokens = createToken(userId, role, accessExpirationTime, refreshExpirationTime);
        String accessToken = tokens[0];
        String refreshToken = tokens[1];
        if (!setTokenInRedis(userId, accessToken, accessExpirationTime, refreshToken, refreshExpirationTime, request, response)) {
            return;
        }

        String redirectUrl = createRedirectUrl(accessToken, userId);

        // token 발급 및 리턴
        getRedirectStrategy().sendRedirect(request,response,redirectUrl);
    }

    private boolean setTokenInRedis(String userId, String accessToken, int accessExpirationTime, String refreshToken, int refreshExpirationTime, HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            redisAuthenticatedUserTemplate.opsForValue().set(userId, accessToken);
            redisAuthenticatedUserTemplate.expire(userId, accessExpirationTime, TimeUnit.MILLISECONDS);

            redisAuthenticatedUserTemplate.opsForValue().set(accessToken, refreshToken);
            redisAuthenticatedUserTemplate.expire(accessToken, refreshExpirationTime, TimeUnit.MILLISECONDS);
            return true;
        } catch (RedisConnectionFailureException ex) {
            sendRedisConnectionFailureException(request, response, ex);
            return false;
        }
    }

    private void sendRedisConnectionFailureException(HttpServletRequest request,HttpServletResponse response,RedisConnectionFailureException ex) throws IOException {
        String redirectUrl = jwtConfig.getFail();

        String errorMessage = ex.getLocalizedMessage();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        redirectUrl = UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam("error", errorMessage)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private String createRedirectUrl(String accessToken, String userId) {
        String redirectUrl = jwtConfig.getSuccess();
        redirectUrl = UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam("userId", userId)
                .queryParam("accessToken", accessToken)
                .build().toUriString();
        return redirectUrl;
    }

    private int[] setExpirationTime() {
        int second = Integer.parseInt(jwtConfig.getSecond());
        int minute = Integer.parseInt(jwtConfig.getMinute());
        int hour = Integer.parseInt(jwtConfig.getHour());

        return new int[] {1000 * second * minute, 1000 * second * minute * hour};
    }

    private String[] createToken(String userId, String role, int accessExpirationTime, int refreshExpirationTime) {
        String accessToken = JWT.create()
                .withSubject("mokumokuAccess")
                .withExpiresAt(new Date(System.currentTimeMillis() + (accessExpirationTime)))
                .withClaim("userId", userId)
                .withClaim("role", role)
                .sign(Algorithm.HMAC512(jwtConfig.getKey()));

        String refreshToken = JWT.create()
                .withSubject("mokumokuRefresh")
                .withExpiresAt(new Date(System.currentTimeMillis() + (refreshExpirationTime)))
                .withClaim("userId", userId)
                .withClaim("role", role)
                .sign(Algorithm.HMAC512(jwtConfig.getKey()));

        return new String[] {accessToken, refreshToken};
    }
}
