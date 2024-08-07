package com.example.mzting.utils;

import com.example.mzting.entity.User;
import com.example.mzting.repository.UserRepository;
import com.example.mzting.dto.UserInformationDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class UserInformationUtils {

    private final UserRepository userRepository;

    public UserInformationUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserInformationDTO getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User)) {
            throw new UnauthorizedException("User not authenticated");
        }

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        return new UserInformationDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }
}