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
    private final RssRepository rssRepository;

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
        rssRepository.save(rss);
        return StateResponse.builder().code("200").message("success").build();
    }

    public List<RssArticleDto> getRssFeedsByCategoryName(String categoryName) {
        List<Rss> allRssRecords = rssRepository.findAll();
        List<RssArticleDto> allRssArticles = new ArrayList<>();

        for (Rss rss : allRssRecords) {
            String rssFeedUrl = getCategoryUrlByName(rss, categoryName);

            if (rssFeedUrl != null) {
                List<RssArticleDto> rssArticles = fetchRssFeed(rssFeedUrl, rss.getPublisher().getId(), rss.getPublisher().getName());
                allRssArticles.addAll(rssArticles);
            }
        }
        return allRssArticles;
    }
    //출판사의 전체 기사 조회
    public List<RssArticleDto> getRssFeedsByPublisher(Long publisherId) {
        Publisher publisher  = publisherRepository.findById(publisherId).orElseThrow(()->new BusinessException(ExceptionCode.PUBLISHER_NOT_FOUND));
        Rss rss = rssRepository.findByPublisher(publisher);
        List<RssArticleDto> allArticles = new ArrayList<>();
        List<String> rssFeedUrls = getAllCategoryUrls(rss);

        for (String rssFeedUrl : rssFeedUrls) {
            if (rssFeedUrl != null) {
                List<RssArticleDto> rssArticles = fetchRssFeed(rssFeedUrl, rss.getPublisher().getId(), rss.getPublisher().getName());
                allArticles.addAll(rssArticles);
            }
        }

        return allArticles;
    }

    private List<RssArticleDto> fetchRssFeed(String rssUrl, Long pubId, String publisherName) {
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
                        .pubId(pubId)
                        .publisherName(publisherName)
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

    private String getCategoryUrlByName(Rss rss, String categoryName) {
        return switch (categoryName.toLowerCase()) {
            case "economy" -> rss.getEconomyUrl();
            case "politics" -> rss.getPoliticsUrl();
            case "society" -> rss.getSocietyUrl();
            case "entertainment" -> rss.getEntertainmentUrl();
            case "culture" -> rss.getCultureUrl();
            case "technology" -> rss.getTechnologyUrl();
            case "opinion" -> rss.getOpinionUrl();
            default -> null;
        };
    }
    private List<String> getAllCategoryUrls(Rss rss) {
        List<String> urls = new ArrayList<>();
        urls.add(rss.getEconomyUrl());
        urls.add(rss.getPoliticsUrl());
        urls.add(rss.getSocietyUrl());
        urls.add(rss.getEntertainmentUrl());
        urls.add(rss.getCultureUrl());
        urls.add(rss.getTechnologyUrl());
        urls.add(rss.getOpinionUrl());
        return urls;
    }

}
