package com.anywayclear.config;

import com.anywayclear.config.jwt.JwtAccessDeniedHandler;
import com.anywayclear.config.jwt.JwtAuthorizationFilter;
import com.anywayclear.config.oauth.CustumOAuth2UserService;
import com.anywayclear.config.oauth.OAuth2AuthenticationSuccessHandler;
import com.anywayclear.config.oauth.OAuth2AuthenticationFailureHandler;
import com.anywayclear.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity // spring security filter가 spring filter chain에 등록됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorized, postAuthorized 어노테이션 활성화 -> 메소드 단위 권한설정 가능
@RequiredArgsConstructor
public class SecurityConfig {


    private final CustumOAuth2UserService custumOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final CorsFilter corsFilter;
    private final JwtConfig jwtConfig;
    private final MemberRepository memberRepository;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // 인증 방식 설정
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable();

        // Cors 필터 추가, CSRF 비활성화
        httpSecurity.addFilterBefore(corsFilter, BasicAuthenticationFilter.class)
                .csrf().disable();

        // JWT Authorization 필터 추가
        httpSecurity
                .addFilter(jwtAuthorizationFilter())
                .exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler());

        // 요청 권한 설정
        httpSecurity
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers(HttpMethod.OPTIONS).permitAll() // OPTIONS 메서드는 모두 허용
                        .anyRequest().permitAll() // 모든 요청 권한 허용 (추후 권한 설정해야함)
                );

        // OAuth2 로그인 설정
        httpSecurity
                .oauth2Login(oauth2Login -> oauth2Login
                                // 로그인 페이지 지정
//            .loginPage("https://mokumoku-git-develop-mokumoku.vercel.app")
                                .userInfoEndpoint()
                                .userService(custumOAuth2UserService)
                                .and()
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                                .failureHandler(oAuth2AuthenticationFailureHandler)
                );

        // 로그아웃 설정
        httpSecurity.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("http://localhost:3000");

        // H2 사용을 위한 설정
        httpSecurity
                .headers().frameOptions().disable();

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JwtAuthorizationFilter(authenticationManagerBean(), memberRepository, jwtConfig);
    }

    @Bean
    public AccessDeniedHandler jwtAccessDeniedHandler() {
        // 커스텀 예외 처리기를 반환하는 빈 정의
        return new JwtAccessDeniedHandler();
    }
}



