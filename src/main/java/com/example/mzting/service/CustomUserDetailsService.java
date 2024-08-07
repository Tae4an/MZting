package com.example.mzting.service;

import com.example.mzting.entity.User;
import com.example.mzting.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * CustomUserDetailsService 클래스
 * 사용자 세부 정보를 로드하는 서비스 클래스
 * OAuth2 인증 사용자 정보도 로드
 */
@Service
public class CustomUserDetailsService extends DefaultOAuth2UserService implements UserDetailsService {

    // 사용자 저장소
    private final UserRepository userRepository;

    /**
     * CustomUserDetailsService 생성자
     * 필요한 의존성을 주입받아 초기화
     *
     * @param userRepository 사용자 저장소
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 사용자 이름으로 사용자 세부 정보를 로드하는 메서드
     *
     * @param username 사용자 이름
     * @return 사용자 세부 정보
     * @throws UsernameNotFoundException 사용자 이름을 찾을 수 없는 경우 예외 발생
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    /**
     * OAuth2 사용자 요청으로 사용자 정보를 로드하는 메서드
     *
     * @param userRequest OAuth2 사용자 요청 객체
     * @return OAuth2 사용자 객체
     * @throws OAuth2AuthenticationException 인증 예외 발생 시
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            user = new User();
            user.setUsername(email); // Here email is used as username
            user.setEmail(email);
            user.setEmailVerified(true); // OAuth2로 로그인한 사용자는 이메일이 이미 확인되었다고 가정
            user = userRepository.save(user);
        }

        return oAuth2User;
    }
}
