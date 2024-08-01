package com.example.mzting.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;

/**
 * JWT 토큰 제공자 클래스
 * JWT 토큰의 생성, 검증 및 관련 유틸리티 메서드를 제공
 */
@Component
public class JwtTokenProvider {

    // JWT 비밀키
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    // JWT 만료 시간 (밀리초 단위)
    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    // 서명 키 객체
    private Key key;

    /**
     * 초기화 메서드
     * JWT 비밀키를 사용하여 서명 키 객체를 초기화
     */
    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * JWT 토큰을 생성하는 메서드
     *
     * @param authentication 인증 객체
     * @return 생성된 JWT 토큰
     */
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * JWT 토큰에서 사용자 이름을 추출하는 메서드
     *
     * @param token JWT 토큰
     * @return 사용자 이름
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    /**
     * JWT 토큰의 유효성을 검증하는 메서드
     *
     * @param token JWT 토큰
     * @return 토큰이 유효한 경우 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // 로그 출력 또는 예외 처리
        }
        return false;
    }

    /**
     * 요청에서 JWT 토큰을 추출하는 메서드
     *
     * @param request HTTP 요청 객체
     * @return JWT 토큰 문자열 또는 null
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * JWT 토큰을 사용하여 인증 객체를 생성하는 메서드
     *
     * @param token JWT 토큰
     * @param userDetails 사용자 정보 객체
     * @return 인증 객체
     */
    public Authentication getAuthentication(String token, UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
