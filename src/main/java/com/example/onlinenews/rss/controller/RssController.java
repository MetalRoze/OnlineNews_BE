package com.example.onlinenews.rss.controller;

import com.example.onlinenews.rss.service.RssService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RssController {

    private final RssService rssService;

    // HTTP GET 요청으로 RSS 피드를 읽는 엔드포인트
    @GetMapping("/fetch-rss")
    public ResponseEntity<?> fetchRssFeed() {
        return ResponseEntity.ok(rssService.fetchRssFeed());
    }
}

