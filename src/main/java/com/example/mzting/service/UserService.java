package com.example.mzting.service;

import com.example.mzting.entity.User;
import com.example.mzting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 사용자 등록, 조회 및 이메일 인증과 관련된 비즈니스 로직을 처리하는 클래스
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    /**
     * 새로운 사용자를 등록하는 메서드
     * 사용자 이름이 이미 존재할 경우 예외를 발생시킴
     * 비밀번호는 암호화되며, 이메일 인증을 위해 이메일이 발송됨
     *
     * @param user 등록할 사용자 객체
     * @return 저장된 사용자 객체
     * @throws IllegalArgumentException 사용자 이름이 이미 존재할 경우 발생하는 예외
     */
    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmailVerified(false); // 이메일 인증 전
        User savedUser = userRepository.save(user);

        // 이메일 인증 발송
        sendVerificationEmail(savedUser);

        return savedUser;
    }

    /**
     * 사용자에게 이메일 인증을 보내는 메서드
     *
     * @param user 이메일 인증을 보낼 사용자 객체
     */
    private void sendVerificationEmail(User user) {
        String email = user.getEmail();
        String verificationLink = "http://localhost:8080/verify-email?email=" + email;
        emailService.sendVerificationEmail(email, verificationLink);
    }

    /**
     * 사용자 이름으로 사용자를 조회하는 메서드
     *
     * @param username 조회할 사용자 이름
     * @return 조회된 사용자 객체
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
