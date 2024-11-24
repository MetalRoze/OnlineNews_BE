package com.example.onlinenews.jwt.controller;

import com.example.onlinenews.jwt.api.JwtApi;
import com.example.onlinenews.jwt.dto.JwtToken;
import com.example.onlinenews.jwt.provider.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController implements JwtApi {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public ResponseEntity<JwtToken> reissue(String refreshToken) {
        return new ResponseEntity<>(jwtTokenProvider.reissueToken(refreshToken), HttpStatus.OK);
    }
}
