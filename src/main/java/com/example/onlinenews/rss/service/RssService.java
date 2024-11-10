package com.example.onlinenews.rss.service;

import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.rss.dto.RssArticleDto;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class RssService {

    private static final String RSS_FEED_URL = "https://www.imaeil.com/rss";

    public List<RssArticleDto> fetchRssFeed() {
        try {
            URL url = new URL(RSS_FEED_URL);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));

            List<SyndEntry> entries = feed.getEntries();
            List<RssArticleDto> rssArticleDtos = new ArrayList<>();
            for (SyndEntry entry : entries) {
                String subtitle = (entry.getDescription() != null) ? entry.getDescription().getValue() : "";
                String createdAt = (entry.getPublishedDate() != null) ? entry.getPublishedDate().toString() : "";

                RssArticleDto rssArticleDto = RssArticleDto.builder()
                        .title(entry.getTitle())
                        .subtitle(subtitle)
                        .url(entry.getLink())
                        .createdAt(createdAt)
                        .author(entry.getAuthor())
                        .build();
                rssArticleDtos.add(rssArticleDto);
            }
            return rssArticleDtos;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

}
