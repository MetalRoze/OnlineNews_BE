package com.example.onlinenews.article.controller;

import com.example.onlinenews.article.api.ArticleAPI;
import com.example.onlinenews.article.dto.ArticleRequestDTO;
import com.example.onlinenews.article.dto.ArticleResponseDTO;
import com.example.onlinenews.article.dto.ArticleUpdateRequestDTO;
import com.example.onlinenews.article.entity.Category;
import com.example.onlinenews.request.api.RequestApi;
import com.example.onlinenews.request.entity.RequestStatus;
import com.example.onlinenews.article.service.ArticleService;
import com.example.onlinenews.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController implements ArticleAPI {
    private final ArticleService articleService;
    private final AuthService authService;

    // 기사 작성
    @Override
    public ResponseEntity<ArticleResponseDTO> createArticle(HttpServletRequest httpServletRequest,
                                                            @RequestPart("requestDTO") ArticleRequestDTO requestDTO,
                                                            @RequestPart("images") List<MultipartFile> images) {
        String email = authService.getEmailFromToken(httpServletRequest);
        return articleService.createArticle(requestDTO, email, images);
    }


    // 전체 기사 조회
    @Override
    public ResponseEntity<List<ArticleResponseDTO>> getAllArticles() {
        return articleService.getAllArticles();
    }

    // 기사 상세 조회
    @Override
    public ResponseEntity<ArticleResponseDTO> getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    // 카테고리 별 기사 조회
    @Override
    public ResponseEntity<List<ArticleResponseDTO>> listArticlesByCategory(@PathVariable Category category) {
        return articleService.listArticlesByCategory(category);
    }

    // 제목 또는 내용으로 기사 검색
    @Override
    public ResponseEntity<List<ArticleResponseDTO>> searchArticles(@RequestParam String title, @RequestParam String content) {
        return articleService.searchArticles(title, content);
    }

    // 사용자 ID로 기사 조회
    @Override
    public ResponseEntity<List<ArticleResponseDTO>> getArticlesByUserId(@PathVariable Long userId) {
        return articleService.getArticlesByUserId(userId);
    }

    // 공개된 기사 조회
    @Override
    public ResponseEntity<List<ArticleResponseDTO>> getPublicArticles() {
        return articleService.getPublicArticles();
    }

    // 상태에 따른 기사 조회
    @Override
    public ResponseEntity<List<ArticleResponseDTO>> getArticlesByState(@PathVariable RequestStatus state) {
        return articleService.getArticlesByState(state);
    }

    // 기사 수정
    @Override
    public ResponseEntity<ArticleResponseDTO> updateArticle(@PathVariable Long id, @RequestBody ArticleUpdateRequestDTO updateRequest) {
        return articleService.updateArticle(id, updateRequest);
    }
}
