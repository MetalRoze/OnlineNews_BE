package com.example.onlinenews.rss.api;

import com.example.onlinenews.rss.dto.RssArticleDto;
import com.example.onlinenews.rss.dto.RssCreateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/article/rss")
@Tag(name = "Article rss", description = "rss 피드 api")
public interface RssApi {

//    @GetMapping("")
//    @Operation(summary = "rss 피드 조회", description = "rss 피드 결과 조회합니다.")
//    List<RssArticleDto> fetchRssFeed();

    @GetMapping("/category")
    @Operation(summary = "rss 피드 카테고리 별로 조회", description = "카테고리 별로 rss 주소를 파싱합니다.")
    List<RssArticleDto> getRssFeedsByCategoryName(@RequestParam String categoryName);

    @GetMapping("/{pubId}")
    @Operation(summary = "rss 피드 카테고리 별로 조회", description = "카테고리 별로 rss 주소를 파싱합니다.")
    List<RssArticleDto> getRssFeedsByPublisher(@PathVariable Long pubId);

    @PostMapping("")
    ResponseEntity<?> create(@RequestBody RssCreateDto rssCreateDto);

}

