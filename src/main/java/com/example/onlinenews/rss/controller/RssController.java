package com.example.onlinenews.rss.controller;

import com.example.onlinenews.rss.api.RssApi;
import com.example.onlinenews.rss.dto.RssArticleDto;
import com.example.onlinenews.rss.dto.RssCreateDto;
import com.example.onlinenews.rss.service.RssService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RssController implements RssApi {

    private final RssService rssService;

//    @Override
//    public List<RssArticleDto> fetchRssFeed() {
//        return rssService.fetchAllRssFeeds();
//    }

    @Override
    public List<RssArticleDto> getRssFeedsByCategoryName(String categoryName) {
        return rssService.getRssFeedsByCategoryName(categoryName);
    }


    @Override
    public ResponseEntity<?> create(RssCreateDto rssCreateDto) {
        return ResponseEntity.ok(rssService.create(rssCreateDto));
    }
}

