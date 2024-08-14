package com.example.mzting.service;

import com.example.mzting.entity.User;
import com.example.mzting.entity.UserCustomImage;
import com.example.mzting.repository.UserCustomImageRepository;
import com.example.mzting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserCustomImageRepository userCustomImageRepository;

    @Autowired
    private EmailService emailService;


    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmailVerified(false); // 이메일 인증 전
        User savedUser = userRepository.save(user);
        UserCustomImage userCustomImage = new UserCustomImage();
        userCustomImage.setId(savedUser.getId());
        userCustomImageRepository.save(userCustomImage);

        // 이메일 인증 발송
        sendVerificationEmail(savedUser);

        return savedUser;
    }

    private void sendVerificationEmail(User user) {
        String email = user.getEmail();
        String verificationLink = "http://localhost:8080/verify-email?email=" + email;
        emailService.sendVerificationEmail(email, verificationLink);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
