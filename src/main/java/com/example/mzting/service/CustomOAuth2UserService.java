package com.example.mzting.service;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * CustomOAuth2UserService 인터페이스
 * OAuth2 인증 사용자 정보를 로드하는 메서드를 정의
 */
public interface CustomOAuth2UserService {

    /**
     * OAuth2 인증 사용자 정보를 로드하는 메서드
     *
     * @param userRequest OAuth2 사용자 요청 객체
     * @return OAuth2 사용자 객체
     * @throws OAuth2AuthenticationException 인증 예외 발생 시
     */
    OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException;
}
