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

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 여기서 JWT 토큰을 생성하고 response에 추가할 수 있습니다.
        String token = jwtTokenProvider.generateToken(authentication);
        response.addHeader("Authorization", "Bearer " + token);
        response.getWriter().write("JWT Token: " + token);

        setDefaultTargetUrl("/qqqaaa");  // 로그인 성공 후 리다이렉트할 URL
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
