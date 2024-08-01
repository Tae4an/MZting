package com.example.mzting.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * EmailService 클래스
 * 이메일 발송을 담당하는 서비스 클래스
 */
@Service
public class EmailService {

    // JavaMailSender 객체
    @Autowired
    private JavaMailSender mailSender;

    /**
     * 이메일 인증 링크를 포함한 인증 이메일을 발송하는 메서드
     *
     * @param to 수신자 이메일 주소
     * @param verificationLink 인증 링크
     */
    public void sendVerificationEmail(String to, String verificationLink) {
        try {
            // MIME 메시지 생성
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // 이메일 설정
            helper.setTo(to);
            helper.setSubject("이메일 인증");
            helper.setText("<p>다음 링크를 클릭하여 이메일을 인증해주세요:</p>" +
                    "<a href=\"" + verificationLink + "\">이메일 인증하기</a>", true);

            // 이메일 발송
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 발송 중 오류가 발생했습니다.", e);
        }
    }
}
