package com.anywayclear.config;

import com.anywayclear.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private MemberService memberService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(Customizer.withDefaults()) // 기본 CORS 설정 적용
                .csrf().disable() // CSRF 비활성화
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers(HttpMethod.OPTIONS).permitAll() // OPTIONS 메서드는 모두 허용
                        .anyRequest().permitAll() // 모든 요청 권한 허용 (추후 권한 설정해야함)
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .userInfoEndpoint()
                        .userService(memberService)
                );
        return httpSecurity.build(); // 설정된 HttpSecurity를 SecurityFilterChain으로 반환
    }
}



