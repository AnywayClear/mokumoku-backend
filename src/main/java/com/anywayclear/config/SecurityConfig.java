package com.anywayclear.config;

import com.anywayclear.config.oauth.CustumOAuth2UserService;
import com.anywayclear.config.oauth.OAuth2AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity // spring security filter가 spring filter chain에 등록됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorized, postAuthorized 어노테이션 활성화 -> 메소드 단위 권한설정 가능
public class SecurityConfig {

    @Autowired
    private CustumOAuth2UserService custumOAuth2UserService;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    private CorsFilter corsFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .addFilterBefore(corsFilter, CorsFilter.class) // CorsFilter를 SecurityFilterChain 앞에 추가
                .csrf().disable() // CSRF 비활성화
                .formLogin().disable() // spring security에서 제공하는 login form 비활성화
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers(HttpMethod.OPTIONS).permitAll() // OPTIONS 메서드는 모두 허용
                        .anyRequest().permitAll() // 모든 요청 권한 허용 (추후 권한 설정해야함)
                )
                .oauth2Login(oauth2Login -> oauth2Login
//                        .loginPage("/frontLoginPage") // OAuth 2.0 로그인을 처리할 때, 인증되지 않은 사용자를 전달할 로그인 페이지를 지정
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                                .userInfoEndpoint()
                                .userService(custumOAuth2UserService)
                ).headers().frameOptions().disable(); // h2사용을 위해 임시 설정

        return httpSecurity.build(); // 설정된 HttpSecurity를 SecurityFilterChain으로 반환
    }
}



