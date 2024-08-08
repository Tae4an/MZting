package com.example.mzting.controller;

import com.example.mzting.entity.User;
import com.example.mzting.repository.UserRepository;
import com.example.mzting.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String email) {
        logger.info("Verifying email: {}", email);
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEmailVerified(true);
            userRepository.save(user);
            logger.info("Email verified successfully: {}", email);
            return ResponseEntity.ok("이메일이 성공적으로 인증되었습니다.");
        }
        logger.warn("Invalid email for verification: {}", email);
        return ResponseEntity.badRequest().body("유효하지 않은 이메일입니다.");
    }

    @GetMapping("/send-verification")
    public ResponseEntity<String> sendVerification(@RequestParam String email) {
        logger.info("Sending verification email to: {}", email);
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (!user.isEmailVerified()) {
                    String verificationLink = "http://your-domain.com/verify-email?email=" + email;
                    emailService.sendVerificationEmail(email, verificationLink);
                    logger.info("Verification email sent to: {}", email);
                    return ResponseEntity.ok("인증 이메일을 발송했습니다. 이메일을 확인해주세요.");
                }
            }
            logger.warn("Email already verified or not found: {}", email);
            return ResponseEntity.badRequest().body("이미 인증된 이메일이거나 유효하지 않은 이메일입니다.");
        } catch (Exception e) {
            logger.error("Error sending verification email to: {}", email, e);
            return ResponseEntity.internalServerError().body("이메일 발송 중 오류가 발생했습니다.");
        }
    }
}