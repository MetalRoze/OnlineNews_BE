package com.example.onlinenews.keyword.api;

import com.example.onlinenews.keyword.dto.KeywordCreateRequestDto;
import com.example.onlinenews.keyword.dto.KeywordDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/keyword")
@Tag(name= "Keyword", description = "사용자 맞춤 키워드 API")
public interface KeywordAPI {
    @GetMapping("")
    @Operation(summary = "사용자 키워드 조회", description = "")
    List<KeywordDto> getKeywordsByUserId(HttpServletRequest servletRequest);

    @PostMapping("/add")
    @Operation(summary = "사용자 키워드 추가", description = "")
    ResponseEntity<?> keywordCreate(HttpServletRequest servletRequest, @RequestBody KeywordCreateRequestDto requestDto);

}
