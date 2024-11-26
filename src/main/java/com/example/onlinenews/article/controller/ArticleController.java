package com.example.onlinenews.article.controller;

import com.example.onlinenews.article.api.ArticleAPI;
import com.example.onlinenews.article.dto.ArticleKeywordDTO;
import com.example.onlinenews.article.dto.ArticleRequestDTO;
import com.example.onlinenews.article.dto.ArticleUpdateRequestDTO;
import com.example.onlinenews.article.entity.Category;
import com.example.onlinenews.keyword.service.KeywordService;
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
    public ResponseEntity<?> createArticle(HttpServletRequest httpServletRequest,
                                                            ArticleRequestDTO requestDTO,
                                                            List<MultipartFile> images) {
        String email = authService.getEmailFromToken(httpServletRequest);
        return articleService.createArticle(requestDTO, email, images);
    }

    // 기사 조회
    @Override
    public ResponseEntity<?> getArticles(Long id, Category category, String title, String content, Long userId, RequestStatus state, Boolean isPublic, String sortBy, String sortDirection) {
        return articleService.getArticles(id, category, title, content, userId, state, isPublic, sortBy, sortDirection);
    }

    // 기사 수정
    @Override
    public ResponseEntity<?> updateArticle(Long id, ArticleUpdateRequestDTO updateRequest, List<MultipartFile> images) {
        return articleService.updateArticle(id, updateRequest, images);
    }

    //기사 키워드 저장
    @Override
    public ResponseEntity<?> keywordCreate(HttpServletRequest servletRequest, Long id, ArticleKeywordDTO requestDto) {
        return ResponseEntity.ok(articleService.articleKeyword(id, requestDto));
    }

    @Override
    public ResponseEntity<?> getKeywords(Long id, HttpServletRequest servletRequest) {
        return articleService.getKeywords(id);

    }

    @Override
    public ResponseEntity<?> deleteKeywords(HttpServletRequest servletRequest, Long id) {
        return ResponseEntity.ok(articleService.deleteKeywords(id));
    }

    @Override
    public ResponseEntity<?> getAllKeywords(HttpServletRequest servletRequest) {
        List<String> keywords = articleService.getAllKeywords();
        return ResponseEntity.ok(keywords);
    }

    @Override
    public void convertToPrivate(HttpServletRequest request, Long articleId) {
        String email = authService.getEmailFromToken(request);
        articleService.convertToPrivate(email,articleId);
    }

    @Override
    public void convertToPublic(HttpServletRequest request, Long articleId) {
        String email = authService.getEmailFromToken(request);
        articleService.convertToPublic(email,articleId);
    }

    @Override
    public boolean getPublicStatus(Long reqId) {
        return  articleService.getPublicStatus(reqId);
    }
}
