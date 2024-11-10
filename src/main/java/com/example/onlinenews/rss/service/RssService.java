package com.example.onlinenews.rss.service;

import com.example.onlinenews.rss.dto.RssArticleDto;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
            int index = 0;

            for (SyndEntry entry : entries) {
                String subtitle = (entry.getDescription() != null) ? entry.getDescription().getValue() : "";
                String createdAt = (entry.getPublishedDate() != null) ? entry.getPublishedDate().toString() : "";

                // createdAt이 null일때는 jsoup파싱
                if (createdAt.isEmpty()) {
                    createdAt = fetchPublishedDateFromHtml(index);
                }

                RssArticleDto rssArticleDto = RssArticleDto.builder()
                        .title(entry.getTitle())
                        .subtitle(subtitle)
                        .url(entry.getLink())
                        .createdAt(createdAt)
                        .author(entry.getAuthor())
                        .build();
                rssArticleDtos.add(rssArticleDto);
                index++;
            }
            return rssArticleDtos;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private String fetchPublishedDateFromHtml(int index) {
        try {
            Document doc = Jsoup.connect(RssService.RSS_FEED_URL).get();
            Element item = doc.select("item").get(index);
            return item.select("pubDate").text();
        } catch (Exception e) {
            return "";
        }
    }
}
