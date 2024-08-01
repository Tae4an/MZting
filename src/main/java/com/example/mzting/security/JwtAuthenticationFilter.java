package com.example.mzting.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증 필터 클래스
 * 각 요청마다 JWT 토큰을 확인하고 인증을 수행
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // JWT 토큰 제공자
    private final JwtTokenProvider jwtTokenProvider;

    // 사용자 정보 서비스
    private final UserDetailsService userDetailsService;

    /**
     * JwtAuthenticationFilter 생성자
     * 필요한 의존성을 주입받아 초기화
     *
     * @param jwtTokenProvider JWT 토큰 제공자
     * @param userDetailsService 사용자 정보 서비스
     */
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * 각 요청마다 필터를 적용하는 메서드
     * JWT 토큰을 확인하고 유효한 경우 인증을 설정
     *
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param filterChain 필터 체인 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 요청에서 JWT 토큰 추출
        String jwt = jwtTokenProvider.resolveToken(request);

        // 토큰이 존재하고 유효한 경우
        if (jwt != null && jwtTokenProvider.validateToken(jwt)) {
            // 토큰에서 사용자 이름 추출
            String username = jwtTokenProvider.getUsernameFromToken(jwt);

            // 사용자 정보를 로드
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 인증 정보를 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터로 요청을 전달
        filterChain.doFilter(request, response);
    }
}
