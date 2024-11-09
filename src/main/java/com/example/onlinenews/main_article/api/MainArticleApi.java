package com.example.onlinenews.main_article.api;

import com.example.onlinenews.main_article.dto.MainArticleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/main-article")
@Tag(name = "MainArticle", description = "메인 화면 기사 API")
public interface MainArticleApi {
    @PostMapping("/{articleId}/select")
    @Operation(summary = "메인 기사 선택", description = "편집장이 기사 중에서 메인화면에 노출할 기사를 지정합니다.")
    ResponseEntity<?> selectArticle(HttpServletRequest request, @PathVariable Long articleId);

    @GetMapping("")
    @Operation(summary = "헤드라인 기사 전체 조회", description = "각 회사에서 헤드라인으로 지정된 기사를 전체 조회합니다.")
    List<MainArticleDto> mainArticleList();
}
