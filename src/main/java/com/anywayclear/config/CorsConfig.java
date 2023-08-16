package com.anywayclear.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    // CORS 필터를 생성하는 빈(Bean)으로 등록
    @Bean
    public CorsFilter corsFilter() {
        // URL 패턴별로 CORS 설정을 다르게 적용할 수 있도록 도와주는 클래스
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // CORS에 관련된 구성 정보를 담고 있는 클래스
        CorsConfiguration config = new CorsConfiguration();

        // 요청과 응답에 인증 정보를 사용할 수 있도록 설정
        config.setAllowCredentials(true);

        // 모든 도메인에서 요청을 허용 (필요에 따라 특정 도메인만 허용 가능)
        config.addAllowedOriginPattern("*");

        // 모든 헤더를 허용 (필요한 경우 특정 헤더만 허용 가능)
        config.addAllowedHeader("*");

        // 모든 exposed 헤더 허용
        config.addExposedHeader("newAccessToken");

        // 모든 HTTP 메서드를 허용 (필요한 경우 특정 메서드만 허용 가능)
        config.addAllowedMethod("*");

        // 모든 경로에 대해 위에서 설정한 CorsConfiguration을 적용
        source.registerCorsConfiguration("/**", config);

        // 위에서 생성한 UrlBasedCorsConfigurationSource를 이용하여 실제로 CORS 필터 생성
        return new CorsFilter(source);
    }
}
