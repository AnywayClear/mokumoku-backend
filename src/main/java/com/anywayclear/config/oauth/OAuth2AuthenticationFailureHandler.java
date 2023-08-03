package com.anywayclear.config.oauth;

import com.anywayclear.config.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final JwtConfig jwtConfig;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String redirectUrl = createRedirectUrl(exception);

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private String createRedirectUrl(AuthenticationException exception) {
        String redirectUrl = jwtConfig.getFail();
        redirectUrl = UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam("error", exception.getLocalizedMessage())
                .build().toUriString();
        return redirectUrl;
    }
}
