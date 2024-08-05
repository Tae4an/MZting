package com.example.mzting.utils;

import com.example.mzting.repository.UserRepository;
import com.example.mzting.dto.UserInformationDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserInformationUtils {

    private final UserRepository userRepository;

    public UserInformationUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserInformationDTO getCurrentUserInfo() throws UnauthorizedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Long uid = userRepository.findIdByUsername(username);
            return new UserInformationDTO(uid, username);
        } else {
            throw new UnauthorizedException("User is not authenticated");
        }
    }

    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }
}