package com.example.onlinenews.keyword.controller;

import com.example.onlinenews.keyword.api.KeywordAPI;
import com.example.onlinenews.keyword.dto.KeywordCreateRequestDto;
import com.example.onlinenews.keyword.dto.KeywordDto;
import com.example.onlinenews.keyword.entity.Keyword;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class KeywordController implements KeywordAPI {
    @Override
    public List<KeywordDto> getKeywordsByUserId(HttpServletRequest servletRequest) {
        return null;
    }

    @Override
    public ResponseEntity<?> keywordCreate(HttpServletRequest servletRequest, KeywordCreateRequestDto requestDto) {
        return null;
    }
}
