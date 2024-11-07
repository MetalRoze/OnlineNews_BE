package com.example.onlinenews.main_article.api;

import com.example.onlinenews.main_article.dto.SelectArticleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/main-article")
@Tag(name = "MainArticle", description = "메인 화면 기사 API")
public interface MainArticleApi {
    @PostMapping("")
    @Operation(summary = "메인 기사 선택", description = "편집장이 기사 중에서 메인화면에 노출할 기사를 지정합니다.")
    ResponseEntity<?> selectArticle(HttpServletRequest request, @RequestBody SelectArticleDto selectArticleDto);
}
