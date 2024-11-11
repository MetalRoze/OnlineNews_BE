package com.example.onlinenews.rss.service;

import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.error.StateResponse;
import com.example.onlinenews.publisher.entity.Publisher;
import com.example.onlinenews.publisher.repository.PublisherRepository;
import com.example.onlinenews.rss.dto.RssArticleDto;
import com.example.onlinenews.rss.dto.RssCreateDto;
import com.example.onlinenews.rss.entity.Rss;
import com.example.onlinenews.rss.repository.RssRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RssService {
    private final PublisherRepository publisherRepository;
    private final RssRepository repository;
    //경제 rss
    private static final String[] RSS_FEED_URLS = {
            "https://www.khan.co.kr/rss/rssdata/economy_news.xml", // 경향신문
            "https://www.kmib.co.kr/rss/data/kmibEcoRss.xml",            // 국민
            "https://rss.donga.com/economy.xml",            // 동아일보
            "https://www.segye.com/Articles/RSSList/segye_economy.xml", //세계
            "https://www.newsis.com/RSS/international.xml",//뉴시스
            "https://www.mediatoday.co.kr/rss/S1N3.xml",  //미디어오늘
            "https://www.sisajournal.com/rss/S1N54.xml", //시사저널
            "https://www.imaeil.com/rss?cate=nations" //매일신문
    };
    public StateResponse create(RssCreateDto rssCreateDto) {
        Publisher publisher  = publisherRepository.findById(rssCreateDto.getPubId()).orElseThrow(()->new BusinessException(ExceptionCode.PUBLISHER_NOT_FOUND));

        Rss rss = Rss.builder()
                .economyUrl(rssCreateDto.getEconomyUrl())
                .politicsUrl(rssCreateDto.getPoliticsUrl())
                .societyUrl(rssCreateDto.getSocietyUrl())
                .entertainmentUrl(rssCreateDto.getEntertainmentUrl())
                .cultureUrl(rssCreateDto.getCultureUrl())
                .technologyUrl(rssCreateDto.getTechnologyUrl())
                .opinionUrl(rssCreateDto.getOpinionUrl())
                .publisher(publisher)
                .build();
        repository.save(rss);
        return StateResponse.builder().code("200").message("success").build();
    }
    public List<RssArticleDto> fetchAllRssFeeds() {
        List<RssArticleDto> allRssArticles = new ArrayList<>();
        for (String rssFeedUrl : RSS_FEED_URLS) {
            List<RssArticleDto> rssArticles = fetchRssFeed(rssFeedUrl);
            allRssArticles.addAll(rssArticles);
        }
        return allRssArticles;
    }

    private List<RssArticleDto> fetchRssFeed(String rssUrl) {
        try {
            URL url = new URL(rssUrl);
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
                    createdAt = fetchPublishedDateFromHtml(index, rssUrl);
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

    private String fetchPublishedDateFromHtml(int index, String rssFeedUrl) {
        try {
            Document doc = Jsoup.connect(rssFeedUrl).get();
            Element item = doc.select("item").get(index);
            return item.select("pubDate").text();
        } catch (Exception e) {
            return "";
        }
    }
}
