package com.example.mzting.service;

import com.example.mzting.entity.User;
import com.example.mzting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * CustomOAuth2UserService 구현 클래스
 * 소셜 로그인 사용자 정보를 로드하고 데이터베이스에 사용자 정보를 저장
 */
@Service
public class CustomOAuth2UserServiceImpl extends DefaultOAuth2UserService implements CustomOAuth2UserService {

    // 사용자 저장소
    @Autowired
    private UserRepository userRepository;

    /**
     * OAuth2 인증 사용자 정보를 로드하는 메서드
     *
     * @param userRequest OAuth2 사용자 요청 객체
     * @return OAuth2 사용자 객체
     * @throws OAuth2AuthenticationException 인증 예외 발생 시
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본 OAuth2 사용자 정보 로드
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 사용자 정보 추출
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String provider = userRequest.getClientRegistration().getRegistrationId();

        // 이메일로 사용자 조회
        User user = userRepository.findByEmail(email);
        if (user == null) {
            // 사용자 정보가 없는 경우 새로 생성하여 저장
            user = new User();
            user.setEmail(email);
            user.setUsername(name);
            user.setEmailVerified(true);  // 소셜 로그인의 경우 이메일이 이미 인증되었다고 가정
            userRepository.save(user);
        }

        // 인증된 사용자 정보 반환
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2User.getAttributes(),
                "email"
        );
    }
}
