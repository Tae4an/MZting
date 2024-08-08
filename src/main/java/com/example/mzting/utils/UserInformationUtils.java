package com.example.mzting.utils;

import com.example.mzting.entity.User;
import com.example.mzting.repository.UserRepository;
import com.example.mzting.dto.UserInformationDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Component
public class UserInformationUtils {

    private static final Logger logger = LoggerFactory.getLogger(UserInformationUtils.class);

    private final UserRepository userRepository;

    public UserInformationUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserInformationDTO getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            logger.error("Authentication object is null");
            throw new UnauthorizedException("User not authenticated");
        }

        if (!(authentication.getPrincipal() instanceof OAuth2User)) {
            logger.error("Principal is not an instance of OAuth2User");
            throw new UnauthorizedException("User not authenticated");
        }

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = getEmailFromOAuth2User(oauth2User);
        if (email == null) {
            logger.error("Email attribute is null");
            throw new UnauthorizedException("Email not found in OAuth2User attributes");
        }

        logger.info("Email found: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new UnauthorizedException("User not found");
                });

        logger.info("User found: {}", user);
        return new UserInformationDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    private String getEmailFromOAuth2User(OAuth2User oauth2User) {
        Map<String, Object> attributes = oauth2User.getAttributes();

        // Google의 경우
        if (attributes.containsKey("email")) {
            return (String) attributes.get("email");
        }

        // Naver의 경우
        if (attributes.containsKey("response")) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            return (String) response.get("email");
        }

        // 다른 OAuth2 프로바이더의 경우 추가

        return null;
    }

    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }
}
