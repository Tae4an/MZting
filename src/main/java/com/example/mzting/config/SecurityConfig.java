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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(ClientRegistrationRepository clientRegistrationRepository,
                          OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                          JwtTokenProvider jwtTokenProvider,
                          CustomUserDetailsService customUserDetailsService) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/**", "/login", "/oauth2/**", "/send-verification", "/verify-email", "/home").permitAll()
                        .anyRequest().authenticated()
                )
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
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

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

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
