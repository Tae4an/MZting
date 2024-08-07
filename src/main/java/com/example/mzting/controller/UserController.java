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

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }

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

    @PostMapping("/complete-profile")
    public ResponseEntity<?> completeProfile(@RequestBody UserProfileUpdateRequest profileUpdateRequest) {
        // 보안 컨텍스트에서 현재 인증 객체를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 디버깅 로그 추가
        System.out.println("Received profile update request: " + profileUpdateRequest);

        // 사용자가 인증되었는지 확인
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            // 인증되지 않은 경우 401 Unauthorized 응답 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        // 인증된 사용자의 이메일을 가져옴
        String email = authentication.getName();

        // 디버깅 로그 추가
        System.out.println("Authenticated user's email: " + email);

        // 이메일로 사용자를 찾음
        Optional<User> optionalUser = userService.findByEmail(email);
        if (optionalUser.isPresent()) {
            // 사용자가 존재하는 경우
            User user = optionalUser.get();

            // 디버깅 로그 추가
            System.out.println("Found user: " + user);

            // 사용자 프로필 정보가 이미 입력된 경우 메인 페이지로 리디렉션
            if (user.getAge() != null && user.getGender() != null && user.getMbti() != null) {
                return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/main")).build();
            }

            // 사용자 프로필 업데이트
            user.setAge(profileUpdateRequest.getAge());
            user.setGender(profileUpdateRequest.getGender());
            user.setMbti(profileUpdateRequest.getMbti());
            // 변경된 사용자 정보 저장
            userService.save(user);
            // 성공적으로 업데이트되었음을 알리는 응답 반환
            return ResponseEntity.ok("Profile updated successfully.");
        } else {
            // 사용자를 찾을 수 없는 경우 400 Bad Request 응답 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
        }
    }

}

@Getter
@Setter
@AllArgsConstructor
class AuthResponse {
    private String token;
}

@Getter
@Setter
class LoginRequest {
    private String username;
    private String password;
}

@Getter
@Setter
class UserProfileUpdateRequest {
    private Integer age;
    private String gender;
    private String mbti;
}
