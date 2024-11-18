package com.example.onlinenews.article.api;

import com.example.onlinenews.article.dto.ArticleKeywordDTO;
import com.example.onlinenews.article.dto.ArticleRequestDTO;
import com.example.onlinenews.article.dto.ArticleUpdateRequestDTO;
import com.example.onlinenews.article.entity.Category;
import com.example.onlinenews.keyword.dto.KeywordCreateRequestDto;
import com.example.onlinenews.request.entity.RequestStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/article")
@Tag(name = "Article", description = "기사 관련 API")
public interface ArticleAPI {

    // 기사 작성
    @PostMapping("write")
    @Operation(summary = "기사 작성", description = "새 기사를 작성합니다.")
    public ResponseEntity<?> createArticle( HttpServletRequest httpServletRequest,
                                                             @RequestPart("requestDTO") ArticleRequestDTO requestDTO,
                                                             @RequestPart(value = "images", required = false) List<MultipartFile> images) ;

    // 기사 검색
    @GetMapping("select")
    @Operation(summary = "기사 조회", description = "검색 조건, 정렬 조건에 따라 기사를 조회합니다.")
    ResponseEntity<?> getArticles(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) RequestStatus state,
            @RequestParam(required = false) Boolean isPublic,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection
    );

    // 기사 수정
    @PatchMapping("update/{id}")
    @Operation(summary = "기사 수정", description = "기사를 수정합니다.")
    ResponseEntity<?> updateArticle(@PathVariable Long id,
                                                     @RequestPart("requestDTO") ArticleUpdateRequestDTO updateRequest,
                                                     @RequestPart(value = "images", required = false) List<MultipartFile> images);

    @PostMapping("{id}/keywords")
    @Operation(summary = "기사 키워드 추출", description = "기사의 키워드를 추출하여 저장합니다.")
    ResponseEntity<?> keywordCreate(HttpServletRequest servletRequest, @PathVariable Long id ,@RequestBody ArticleKeywordDTO requestDto);

    @PatchMapping("/{articleId}/private")
    @Operation(summary = "기사 비공개 ", description = "편집장이 기사를 비공개합니다.")
    boolean convertToPrivate(HttpServletRequest request, @PathVariable Long articleId);

    @PatchMapping("/{articleId}/public")
    @Operation(summary = "기사 공개 ", description = "편집장이 기사를 공개합니다.")
    boolean convertToPublic(HttpServletRequest request, @PathVariable Long articleId);

    @GetMapping("/{articleId}/public-status")
    @Operation(summary = "현재 공개/비공개 상태 조회", description = "해당 기사의 공개, 비공개 상태를 조회합니다. (true->공개)")
    boolean getPublicStatus(@PathVariable Long articleId);

}
