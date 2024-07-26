package com.example.mzting.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 여기서 JWT 토큰을 생성하고 response에 추가할 수 있습니다.
        // 또는 세션을 생성하거나 다른 필요한 작업을 수행할 수 있습니다.

        setDefaultTargetUrl("/home");  // 로그인 성공 후 리다이렉트할 URL
        super.onAuthenticationSuccess(request, response, authentication);
    }
}