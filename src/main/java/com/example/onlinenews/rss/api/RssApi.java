package com.example.onlinenews.rss.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/article/rss")
@Tag(name = "Article rss", description = "rss 피드 api")
public interface RssApi {

    @GetMapping("")
    @Operation(summary = "rss 피드 조회", description = "rss 피드 결과 조회합니다.")
    ResponseEntity<?> fetchRssFeed();
}

