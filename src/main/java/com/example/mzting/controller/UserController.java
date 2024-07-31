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
