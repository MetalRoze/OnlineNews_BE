package com.example.onlinenews.keyword.controller;

import com.example.onlinenews.jwt.provider.JwtTokenProvider;
import com.example.onlinenews.keyword.api.KeywordAPI;
import com.example.onlinenews.keyword.dto.KeywordCreateRequestDto;
import com.example.onlinenews.keyword.dto.KeywordDto;
import com.example.onlinenews.keyword.entity.Keyword;
import com.example.onlinenews.keyword.service.KeywordService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class KeywordController implements KeywordAPI {
    private final KeywordService keywordService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public List<KeywordDto> getKeywordsByUserId(HttpServletRequest servletRequest)
    {
        String email = jwtTokenProvider.getAccount(jwtTokenProvider.resolveToken(servletRequest));
        return keywordService.getById(email);
    }

    @Override
    public ResponseEntity<?> keywordCreate(HttpServletRequest servletRequest, KeywordCreateRequestDto requestDto) {
        String email = jwtTokenProvider.getAccount(jwtTokenProvider.resolveToken(servletRequest));
        return ResponseEntity.ok(keywordService.keywordCreate(email, requestDto));
    }
}
