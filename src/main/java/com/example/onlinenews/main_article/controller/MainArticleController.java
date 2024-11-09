package com.example.onlinenews.main_article.controller;

import com.example.onlinenews.article.entity.Category;
import com.example.onlinenews.main_article.api.MainArticleApi;
import com.example.onlinenews.main_article.dto.MainArticleDto;
import com.example.onlinenews.main_article.service.MainArticleService;
import com.example.onlinenews.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainArticleController implements MainArticleApi {
    private final MainArticleService mainArticleService;
    private final  AuthService authService;
    @Override
    public ResponseEntity<?> selectArticle(HttpServletRequest request, Long articleId) {
        String email = authService.getEmailFromToken(request);
        return ResponseEntity.ok(mainArticleService.selectArticle(email, articleId));
    }

    @Override
    public List<MainArticleDto> mainArticleList() {
        return mainArticleService.mainArticles();
    }

    @Override
    public List<MainArticleDto> mainArticlesByCategory(Category category) {
        return mainArticleService.mainArticlesByCategory(category);
    }
}
