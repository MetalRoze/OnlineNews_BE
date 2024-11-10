package com.example.onlinenews.article.controller;

import com.example.onlinenews.article.api.ArticleAPI;
import com.example.onlinenews.article.dto.ArticleRequestDTO;
import com.example.onlinenews.article.dto.ArticleUpdateRequestDTO;
import com.example.onlinenews.article.entity.Category;
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

    @Override
    public ResponseEntity<?> getArticles(Long id, Category category, String title, String content, Long userId, RequestStatus state, Boolean isPublic, String sortBy, String sortDirection) {
        return articleService.getArticles(id, category, title, content, userId, state, isPublic, sortBy, sortDirection);
    }

    // 기사 수정
    @Override
    public ResponseEntity<?> updateArticle(Long id, ArticleUpdateRequestDTO updateRequest, List<MultipartFile> images) {
        return articleService.updateArticle(id, updateRequest, images);
    }
}
