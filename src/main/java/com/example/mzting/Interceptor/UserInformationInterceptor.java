package com.example.mzting.Interceptor;

import com.example.mzting.dto.UserInformationDTO;
import com.example.mzting.utils.UserInformationUtils;
import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
public class UserInformationInterceptor implements HandlerInterceptor {

    private final UserInformationUtils userInformationUtils;

    public UserInformationInterceptor(UserInformationUtils userInformationUtils) {
        this.userInformationUtils = userInformationUtils;
    }

    @Override
    public boolean preHandle(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler) throws Exception {
        try {
            UserInformationDTO userInfo = userInformationUtils.getCurrentUserInfo();
            if (userInfo == null || userInfo.getUid() == null) {
                Objects.requireNonNull(response).setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("User not authenticated");
                return false;
            }
            Objects.requireNonNull(request).setAttribute("username", userInfo.getUsername());
            Objects.requireNonNull(request).setAttribute("uid", userInfo.getUid());
            return true;
        } catch (UserInformationUtils.UnauthorizedException e) {
            Objects.requireNonNull(response).setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(e.getMessage());
            return false;
        }
    }
}
