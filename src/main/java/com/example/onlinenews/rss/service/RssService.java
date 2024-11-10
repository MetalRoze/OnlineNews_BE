package com.example.onlinenews.rss.service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;

@Service
public class RssService {

    private static final String RSS_FEED_URL = "https://www.imaeil.com/rss";  // 매일신문 RSS URL 예시

    public String fetchRssFeed() {
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(RSS_FEED_URL);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));

            List<SyndEntry> entries = feed.getEntries();
            for (SyndEntry entry : entries) {
                response.append("Title: ").append(entry.getTitle()).append("\n");
                response.append("Link: ").append(entry.getLink()).append("\n");
                response.append("Published Date: ").append(entry.getPublishedDate()).append("\n");
                response.append("Description: ").append(entry.getDescription().getValue()).append("\n");
                response.append("---------------------------------------------------\n");
            }
        } catch (Exception e) {
            response.append("Error: ").append(e.getMessage());
        }

        return response.toString();
    }
}
