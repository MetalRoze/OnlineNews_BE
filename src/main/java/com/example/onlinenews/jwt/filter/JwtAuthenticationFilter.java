package com.example.onlinenews.jwt.filter;

import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.jwt.provider.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Jwt가 유효성을 검증하는 Filter
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtProvider.resolveToken(request);
        System.out.println("Resolved Token: " + token);  // 토큰이 올바르게 파싱되는지 확인

        if (token != null && jwtProvider.validateToken(token)) {
            Authentication auth = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else if(token != null && !jwtProvider.validateToken(token)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return; // 인증 실패 시 필터 체인을 더 진행하지 않음
        }

        filterChain.doFilter(request, response);
    }
}