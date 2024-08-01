package com.example.mzting.config;

import com.example.mzting.security.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * OAuth2 인증 성공 처리기 클래스
 * OAuth2 인증 성공 시 JWT 토큰을 생성하고 응답에 추가하는 역할을 수행
 */
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // JWT 토큰 생성 제공자
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * OAuth2AuthenticationSuccessHandler 생성자
     * JwtTokenProvider를 주입받아 초기화
     *
     * @param jwtTokenProvider JWT 토큰 제공자
     */
    @Autowired
    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 인증 성공 시 호출되는 메서드
     * JWT 토큰을 생성하고 응답 헤더에 추가
     *
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param authentication 인증 객체
     * @throws IOException 입출력 예외
     * @throws ServletException 서블릿 예외
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // JWT 토큰 생성
        String token = jwtTokenProvider.generateToken(authentication);

        // 응답 헤더에 JWT 토큰 추가
        response.addHeader("Authorization", "Bearer " + token);

        // 응답 본문에 JWT 토큰 추가
        response.getWriter().write("JWT Token: " + token);

        // 로그인 성공 후 리다이렉트할 URL 설정
        setDefaultTargetUrl("/qqqaaa");

        // 부모 클래스의 onAuthenticationSuccess 메서드 호출
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
