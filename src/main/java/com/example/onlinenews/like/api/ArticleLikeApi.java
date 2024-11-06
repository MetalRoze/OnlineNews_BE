package com.example.onlinenews.like.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/article")
@Tag(name = "Article", description = "좋아요 API")
public interface ArticleLikeApi {

    @PostMapping("/{articleId}/like")
    @Operation(summary = "기사 좋아요", description = "기사를 좋아요합니다.")
    ResponseEntity<?> likeCreate(HttpServletRequest request, @PathVariable Long articleId);

}
