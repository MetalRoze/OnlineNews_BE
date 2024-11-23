package com.example.onlinenews.like.controller;

import com.example.onlinenews.jwt.provider.JwtTokenProvider;
import com.example.onlinenews.like.api.ArticleLikeApi;
import com.example.onlinenews.like.dto.ArticleLikeDto;
import com.example.onlinenews.like.service.ArticleLikeService;
import com.example.onlinenews.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleLikeController implements ArticleLikeApi {

    private  final ArticleLikeService articleLikeService;
    private final AuthService authService;

    @Override
    public ResponseEntity<?> likeCreate(HttpServletRequest request, Long articleId) {
        String email = authService.getEmailFromToken(request);
        return ResponseEntity.ok(articleLikeService.likeCreate(email, articleId));
    }

    @Override
    public List<ArticleLikeDto> myLikes(HttpServletRequest request) {
        String email = authService.getEmailFromToken(request);
        return articleLikeService.myLikes(email);
    }

    @Override
    public List<ArticleLikeDto> articleLikes(Long articleId) {
        return articleLikeService.articleLikes(articleId);
    }

    @Override
    public ResponseEntity<?> deleteLike(HttpServletRequest request, Long articleLikeId) {
        String email = authService.getEmailFromToken(request);
        return ResponseEntity.ok(articleLikeService.deleteLike(email, articleLikeId));
    }

    @Override
    public Long checkLike(HttpServletRequest request, Long articleId) {
        String email = authService.getEmailFromToken(request);
        return articleLikeService.checkLike(email, articleId);
    }



}
