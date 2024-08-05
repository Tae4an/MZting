package com.example.mzting.config;

import com.example.mzting.entity.User;
import com.example.mzting.repository.UserRepository;
import com.example.mzting.security.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // JWT 토큰 생성
        String token = jwtTokenProvider.generateToken(authentication);

        // 로그인된 계정의 이메일 주소를 콘솔에 출력 및 DB에 사용자 저장
        if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = oAuth2User.getAttributes();
            String email;
            String name = (String) attributes.get("name");
            String provider = oAuth2User.getAuthorities().toString().contains("google") ? "google" : "naver";

            if (attributes.containsKey("response")) {
                Map<String, Object> responseMap = (Map<String, Object>) attributes.get("response");
                email = (String) responseMap.get("email");
                name = (String) responseMap.get("name");
                provider = "naver";
            } else {
                email = (String) attributes.get("email");
            }

            System.out.println("Logged in with OAuth2 account: " + email);

            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) {
                User user = new User();
                user.setProviderId(oAuth2User.getName());
                user.setUsername(name);
                user.setEmail(email);
                user.setPassword(passwordEncoder.encode(generateTemporaryPassword()));
                user.setProvider(provider);
                userRepository.save(user);
            }

        } else {
            String email = authentication.getName();
            System.out.println("Logged in with local account: " + email);
        }

        // 토큰을 응답 헤더에 추가
        response.addHeader("Authorization", "Bearer " + token);

        // 토큰을 응답 본문에 추가
        response.getWriter().write("JWT Token: " + token);

        // 로그인 성공 후 리다이렉트할 URL 설정
        setDefaultTargetUrl("/qqqaaa");
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private String generateTemporaryPassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[24];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
