package com.example.onlinenews.jwt.filter;

import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.jwt.provider.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Jwt가 유효성을 검증하는 Filter
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = jwtProvider.resolveToken(request);
        System.out.println("Resolved Token: " + token);  // 토큰이 올바르게 파싱되는지 확인

        if (token != null) {
            try {
                if (jwtProvider.validateToken(token)) {
                    Authentication auth = jwtProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (BusinessException e) {
                // 예외 종류에 따라 다른 상태 코드 처리
                if (e.getExceptionCode() == ExceptionCode.TOKEN_EXPIRED) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 만료된 토큰에 대해서 401 처리
                    response.getWriter().write("Token expired. Please refresh your token.");
                } else if (e.getExceptionCode() == ExceptionCode.TOKEN_NOT_VALID) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);  // 토큰이 유효하지 않은 경우 403 처리
                    response.getWriter().write("Invalid token.");
                }
                return; // 인증 실패 시 필터 체인을 더 진행하지 않음
            }
        }

        filterChain.doFilter(request, response);
    }
}