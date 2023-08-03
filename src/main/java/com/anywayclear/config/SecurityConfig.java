package com.anywayclear.config;

import com.anywayclear.config.jwt.JwtAuthorizationFilter;
import com.anywayclear.config.oauth.CustumOAuth2UserService;
import com.anywayclear.config.oauth.OAuth2AuthenticationSuccessHandler;
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
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity // spring security filter가 spring filter chain에 등록됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorized, postAuthorized 어노테이션 활성화 -> 메소드 단위 권한설정 가능
@RequiredArgsConstructor
public class SecurityConfig {


    private final CustumOAuth2UserService custumOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final CorsFilter corsFilter;
    private final MemberRepository memberRepository;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 방식 인증 비활성화
                .and()
                .addFilter(corsFilter) // CorsFilter를 SecurityFilterChain 앞에 추가
                .csrf().disable()
                .formLogin().disable() // spring security에서 제공하는 login form 비활성화
                .httpBasic().disable() // Basic 방식 인증 인가 비활성화
                .addFilter(jwtAuthorizationFilter())
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers(HttpMethod.OPTIONS).permitAll() // OPTIONS 메서드는 모두 허용
                        .anyRequest().permitAll() // 모든 요청 권한 허용 (추후 권한 설정해야함)
                )
                .oauth2Login(oauth2Login -> oauth2Login
//                        .loginPage("https://mokumoku-git-develop-mokumoku.vercel.app") // OAuth 2.0 로그인을 처리할 때, 인증되지 않은 사용자를 전달할 로그인 페이지를 지정
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                                .userInfoEndpoint()
                                .userService(custumOAuth2UserService)
                ).headers().frameOptions().disable(); // h2사용을 위해 임시 설정

        return httpSecurity.build(); // 설정된 HttpSecurity를 SecurityFilterChain으로 반환
    }
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JwtAuthorizationFilter(authenticationManagerBean(), memberRepository);
    }
}



