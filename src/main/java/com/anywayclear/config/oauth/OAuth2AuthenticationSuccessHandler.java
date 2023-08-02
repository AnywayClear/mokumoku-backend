package com.anywayclear.config.oauth;

import com.anywayclear.config.JwtConfig;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtConfig jwtConfig;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        System.out.println("OAuth2AuthenticationSuccessHandler : 로그인 성공");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // RSA방식은 아니고 Hash 암호 방식
        String jwtToken = JWT.create()
                .withSubject("mokumoku")
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24)))
                .withClaim("userId", (String) oAuth2User.getAttributes().get("userId"))
                .withClaim("role", (String) oAuth2User.getAttributes().get("role"))
                .sign(Algorithm.HMAC512(jwtConfig.getKey()));
        System.out.println("jwtToken = " + jwtToken);
        response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + " " + jwtToken);
//        this.getSuccessHandler().onAuthenticationSuccess(request, response, authentication);
    }
}
