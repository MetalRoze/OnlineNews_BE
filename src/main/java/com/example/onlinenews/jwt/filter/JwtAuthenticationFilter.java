package com.example.onlinenews.jwt.filter;

import com.example.onlinenews.error.BusinessException;
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
            throws ServletException, IOException, BusinessException {
        String token = jwtProvider.resolveToken(request);
        System.out.println("Resolved Token: " + token);  // 토큰이 올바르게 파싱되는지 확인

        try {
            if (token != null) {
                if (jwtProvider.validateToken(token)) {
                    Authentication auth = jwtProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (BusinessException e) {
            response.sendError(e.getExceptionCode().getStatus(), e.getExceptionCode().getMessage());
        }

        filterChain.doFilter(request, response);
    }
}