package com.example.onlinenews.config;

import com.example.onlinenews.jwt.filter.JwtAuthenticationFilter;
import com.example.onlinenews.jwt.provider.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests((registry) ->
                        registry.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Swagger 관련 경로 허용
                                //이 사이에 권한 인증 요구 추가 .requestMatchers(<특정 API>).authentication()
                                .requestMatchers("/api/admin/**").hasAnyRole("SYSTEM_ADMIN", "EDITOR") // 시스템 관리자만 접근 가능
                                .requestMatchers("/api/user/**").permitAll()
                                .anyRequest().permitAll()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(new AccessDeniedHandler() {
                            @Override
                            public void handle(HttpServletRequest request, HttpServletResponse response,
                                               AccessDeniedException accessDeniedException)
                                    throws IOException, ServletException, IOException {
                                response.setStatus(403);
                                response.setCharacterEncoding("utf-8");
                                response.setContentType("text/html; charset=UTF-8");
                                response.getWriter().write("권한이 없는 사용자입니다.");
                            }
                        })
                        .authenticationEntryPoint(new AuthenticationEntryPoint() {
                            @Override
                            public void commence(HttpServletRequest request, HttpServletResponse response,
                                                 AuthenticationException authException)
                                    throws IOException, ServletException {
                                response.setStatus(401);
                                response.setCharacterEncoding("utf-8");
                                response.setContentType("text/html; charset=UTF-8");
                                response.getWriter().write("인증되지 않은 사용자입니다.");
                            }
                        })
                )
                .build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedOrigin("http://localhost:5173");
        configuration.addAllowedOrigin("http://myeongbo.site");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}