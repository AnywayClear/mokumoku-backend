package com.anywayclear.config.oauth;

import com.anywayclear.config.jwt.JwtProperties;
import com.anywayclear.config.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        String jwt = jwtProvider.createToken(authentication);
        String redirectUrl = "http://localhost:3000/oauth2/redirect";

        response.setHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwt);
        response.setStatus(HttpServletResponse.SC_OK);

        if (response.isCommitted()) {
            logger.debug(redirectUrl + "로 리다이렉트 불가");
            return;
        }

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);

    }
}
