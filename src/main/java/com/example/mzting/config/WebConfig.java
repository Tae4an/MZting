package com.example.mzting.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 웹 설정 클래스
 * 애플리케이션의 CORS 설정을 구성
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * CORS 매핑 설정 메서드
     * 특정 경로 패턴에 대한 CORS 설정을 정의
     *
     * @param registry CORS 레지스트리 객체
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 허용할 오리진 설정
                .allowedOrigins("http://localhost:3000")
                // 허용할 HTTP 메서드 설정
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // 허용할 헤더 설정
                .allowedHeaders("*")
                // 자격 증명 허용 설정
                .allowCredentials(true);
    }
}
