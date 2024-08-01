package com.example.mzting.controller;

import com.example.mzting.security.JwtTokenProvider;
import com.example.mzting.service.UserService;
import com.example.mzting.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 인증 및 관리와 관련된 요청을 처리하는 컨트롤러 클래스
 * 사용자 등록, 로그인, 현재 사용자 조회와 관련된 API 엔드포인트를 정의
 */
@RestController
@RequestMapping("/api/auth")
public class UserController {

    // 사용자 서비스
    @Autowired
    private UserService userService;

    // 인증 관리자
    @Autowired
    private AuthenticationManager authenticationManager;

    // JWT 토큰 제공자
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * 사용자 등록 엔드포인트
     * 주어진 사용자 정보를 바탕으로 새로운 사용자를 등록
     *
     * @param user 사용자 정보 객체
     * @return 등록된 사용자 정보 또는 오류 메시지를 포함한 ResponseEntity 객체
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }

    /**
     * 사용자 로그인 엔드포인트
     * 주어진 로그인 요청 정보를 바탕으로 사용자를 인증하고 JWT 토큰을 반환
     *
     * @param loginRequest 로그인 요청 정보 객체
     * @return JWT 토큰 또는 오류 메시지를 포함한 ResponseEntity 객체
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtTokenProvider.generateToken(authentication);

            return ResponseEntity.ok(new AuthResponse(jwt));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: Invalid username or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login");
        }
    }

    /**
     * 현재 인증된 사용자 정보를 조회하는 엔드포인트
     *
     * @return 현재 사용자 이름 또는 인증되지 않은 상태에 대한 메시지를 포함한 ResponseEntity 객체
     */
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return ResponseEntity.ok(userDetails.getUsername());
        } else {
            return ResponseEntity.ok(principal.toString());
        }
    }
}

/**
 * 인증 응답을 담는 DTO 클래스
 */
@Getter
@Setter
@AllArgsConstructor
class AuthResponse {
    private String token;
}

/**
 * 로그인 요청 정보를 담는 DTO 클래스
 */
@Getter
@Setter
class LoginRequest {
    private String username;
    private String password;
}
