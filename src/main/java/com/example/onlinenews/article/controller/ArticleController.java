package com.example.onlinenews.article.controller;

import com.example.onlinenews.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    @PostMapping("/api/article/create")
    public void create(){
        articleService.create();
    }
}
