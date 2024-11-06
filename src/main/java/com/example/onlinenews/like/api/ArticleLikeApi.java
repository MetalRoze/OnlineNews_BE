package com.example.onlinenews.like.api;

import com.example.onlinenews.like.dto.ArticleLikeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/article")
@Tag(name = "Article", description = "좋아요 API")
public interface ArticleLikeApi {

    @PostMapping("/{articleId}/like")
    @Operation(summary = "기사 좋아요", description = "기사를 좋아요합니다.")
    ResponseEntity<?> likeCreate(HttpServletRequest request, @PathVariable Long articleId);


    @GetMapping("/like/me")
    @Operation(summary = "좋아요 내역 조회", description = "사용자가 좋아요 한 기사 내역을 조회합니다")
    List<ArticleLikeDto> myLikes(HttpServletRequest request);
}
