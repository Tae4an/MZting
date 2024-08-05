package com.example.mzting.config;

import com.example.mzting.Interceptor.UserInformationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 웹 설정 클래스
 * 애플리케이션의 CORS 설정을 구성
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final UserInformationInterceptor userInformationInterceptor;

    public WebConfig (UserInformationInterceptor userInformationInterceptor) {
        this.userInformationInterceptor = userInformationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInformationInterceptor)
                .addPathPatterns("/api/**")  // 인증이 필요한 경로 패턴을 지정
                .excludePathPatterns("/api/auth/**");  // 인증이 필요 없는 경로 패턴을 제외
    }

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
