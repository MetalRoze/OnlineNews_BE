package com.example.onlinenews.user.service;

import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.jwt.provider.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String getEmailFromToken(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null) {
            throw new BusinessException(ExceptionCode.TOKEN_NOT_VALID);
        }
        if (!jwtTokenProvider.validateToken(token)) {
            throw new BusinessException(ExceptionCode.TOKEN_EXPIRED);
        }
        return jwtTokenProvider.getAccount(token);
    }
}
