package com.example.onlinenews.rss.controller;

import com.example.onlinenews.rss.api.RssApi;
import com.example.onlinenews.rss.dto.RssCreateDto;
import com.example.onlinenews.rss.service.RssService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RssController implements RssApi {

    private final RssService rssService;

    @Override
    public ResponseEntity<?> fetchRssFeed() {
        return ResponseEntity.ok(rssService.fetchAllRssFeeds());
    }

    @Override
    public ResponseEntity<?> create(RssCreateDto rssCreateDto) {
        return ResponseEntity.ok(rssService.create(rssCreateDto));
    }
}

