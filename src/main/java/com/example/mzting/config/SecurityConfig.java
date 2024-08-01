package com.example.mzting.config;

import com.example.mzting.security.JwtAuthenticationFilter;
import com.example.mzting.security.JwtTokenProvider;
import com.example.mzting.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Spring Security 설정 클래스
 * 애플리케이션의 보안 설정을 구성
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // OAuth2 클라이언트 등록 저장소
    private final ClientRegistrationRepository clientRegistrationRepository;

    // OAuth2 인증 성공 처리기
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    // JWT 토큰 제공자
    private final JwtTokenProvider jwtTokenProvider;

    // 사용자 정의 인증 서비스
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * SecurityConfig 생성자
     * 필요한 의존성을 주입받아 초기화
     *
     * @param clientRegistrationRepository OAuth2 클라이언트 등록 저장소
     * @param oAuth2AuthenticationSuccessHandler OAuth2 인증 성공 처리기
     * @param jwtTokenProvider JWT 토큰 제공자
     * @param customUserDetailsService 사용자 정의 인증 서비스
     */
    public SecurityConfig(ClientRegistrationRepository clientRegistrationRepository,
                          OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                          JwtTokenProvider jwtTokenProvider,
                          CustomUserDetailsService customUserDetailsService) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * 보안 필터 체인 설정 메서드
     * 보안 필터 체인을 구성하고 반환
     *
     * @param http HttpSecurity 객체
     * @return 구성된 SecurityFilterChain 객체
     * @throws Exception 예외
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 보호 비활성화
                .csrf(csrf -> csrf.disable())

                // CORS 설정
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 요청 권한 설정
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/**", "/login", "/oauth2/**", "/send-verification", "/verify-email", "/home").permitAll()
                        .anyRequest().authenticated()
                )

                // OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .authorizationEndpoint(authorizationEndpoint ->
                                authorizationEndpoint.authorizationRequestResolver(
                                        new OAuth2AuthorizationRequestResolver() {
                                            private final OAuth2AuthorizationRequestResolver defaultResolver =
                                                    new DefaultOAuth2AuthorizationRequestResolver(
                                                            clientRegistrationRepository,
                                                            "/oauth2/authorization"
                                                    );

                                            @Override
                                            public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
                                                OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request);
                                                return authorizationRequest;
                                            }

                                            @Override
                                            public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
                                                OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request, clientRegistrationId);
                                                return authorizationRequest;
                                            }
                                        }
                                )
                        )
                )

                // JWT 인증 필터 추가
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS 설정 소스 메서드
     * CORS 구성을 정의하고 반환
     *
     * @return 설정된 CorsConfigurationSource 객체
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 인증 관리자 빈 설정 메서드
     * AuthenticationManager를 설정하고 반환
     *
     * @param authenticationConfiguration 인증 구성 객체
     * @return 설정된 AuthenticationManager 객체
     * @throws Exception 예외
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 비밀번호 인코더 빈 설정 메서드
     * BCryptPasswordEncoder를 설정하고 반환
     *
     * @return 설정된 PasswordEncoder 객체
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
