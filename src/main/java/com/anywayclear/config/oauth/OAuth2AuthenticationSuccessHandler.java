package com.anywayclear.config.oauth;

import com.anywayclear.config.JwtConfig;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

        if (response.isCommitted()) {
            log.debug("Response has already been committed");
            return;
        }

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String userId = (String) oAuth2User.getAttributes().get("userId");
        String role = (String) oAuth2User.getAttributes().get("role");
        int expirationTime = setExpirationTime();
        System.out.println("expirationTime = " + expirationTime);

        String accessToken = createToken(userId, role, expirationTime);
        setTokenInRedis(userId, accessToken, expirationTime);
        System.out.println("accessToken = " + accessToken);

        String redirectUrl = createRedirectUrl(accessToken, userId);

        getRedirectStrategy().sendRedirect(request,response,redirectUrl);
    }

    private void setTokenInRedis(String userId, String token, int expirationTime) {
        redisAuthenticatedUserTemplate.opsForValue().set(userId, token);
        redisAuthenticatedUserTemplate.expire(userId, expirationTime, TimeUnit.MILLISECONDS);
    }

    private String createRedirectUrl(String accessToken, String userId) {
        String redirectUrl = jwtConfig.getSuccess();
        redirectUrl = UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam("userId", userId)
                .queryParam("accessToken", accessToken)
                .build().toUriString();
        return redirectUrl;
    }

    private int setExpirationTime() {
        int second = Integer.parseInt(jwtConfig.getSecond());
        int minute = Integer.parseInt(jwtConfig.getMinute());
        int hour = Integer.parseInt(jwtConfig.getHour());

        return 1000 * second * minute * hour;
    }

    private String createToken(String userId, String role, int expirationTime) {
        String accessToken = JWT.create()
                .withSubject("mokumoku")
                .withExpiresAt(new Date(System.currentTimeMillis() + (expirationTime)))
                .withClaim("userId", userId)
                .withClaim("role", role)
                .sign(Algorithm.HMAC512(jwtConfig.getKey()));
        return accessToken;
    }
}
