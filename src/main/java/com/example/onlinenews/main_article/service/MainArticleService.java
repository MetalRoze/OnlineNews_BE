package com.example.onlinenews.main_article.service;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.article.entity.Category;
import com.example.onlinenews.article.repository.ArticleRepository;
import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.error.StateResponse;
import com.example.onlinenews.main_article.dto.MainArticleDto;
import com.example.onlinenews.main_article.entity.MainArticle;
import com.example.onlinenews.main_article.repository.MainArticleRepository;
import com.example.onlinenews.user.entity.User;
import com.example.onlinenews.user.entity.UserGrade;
import com.example.onlinenews.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainArticleService {
    private final MainArticleRepository mainArticleRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public StateResponse selectArticle(String email, Long articleId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        checkEditorPermission(user);

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND));

        MainArticle mainArticle = MainArticle.builder()
                .publisher(user.getPublisher())
                .article(article)
                .createdAt(LocalDateTime.now())
                .category(article.getCategory())
                .build();
        mainArticleRepository.save(mainArticle);
        return StateResponse.builder().code("200").message("success").build();
    }

    public List<MainArticleDto> mainArticles() {
        return mainArticleRepository.findAllByOrderByArticleViewsDesc().stream()
                .map(MainArticleDto::fromEntity)
                .collect(Collectors.toList());
    }

    //메인 화면 헤드라인 하나
    public List<MainArticleDto> mainHeadline() {
        return mainArticleRepository.findAllByOrderByArticleViewsDesc().stream()
                .limit(1)
                .map(MainArticleDto::fromEntity)
                .collect(Collectors.toList());
    }

    //메인에 카테고리 별로 2개
    public List<MainArticleDto> mainTwoArticlesByCategory(Category category) {
        Category headlineCategory = getHeadLineCategory();

        if (headlineCategory != category) {
            return mainArticleRepository.findByCategoryOrderByArticleViewsDesc(category).stream()
                    .limit(2)
                    .map(MainArticleDto::fromEntity)
                    .collect(Collectors.toList());
        } else {
            return mainArticleRepository.findByCategoryOrderByArticleViewsDesc(category).stream()
                    .skip(1)
                    .limit(2)
                    .map(MainArticleDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    public List<MainArticleDto> mainArticleByCategory(Category category) {
        return mainArticleRepository.findByCategoryOrderByArticleViewsDesc(category).stream()
                .limit(1)
                .map(MainArticleDto::fromEntity)
                .collect(Collectors.toList());
    }

    private void checkEditorPermission(User user) {
        if (user.getGrade().getValue() < UserGrade.EDITOR.getValue()) {
            throw new BusinessException(ExceptionCode.USER_NOT_ALLOWED);
        }
    }

    public List<MainArticle> getHeadArticlesForToday() {
        List<MainArticle> headArticles = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Category category : Category.values()) {
            List<MainArticle> articles = mainArticleRepository
                    .findTop1ByCategoryAndCreatedAtBetweenOrderByArticleViewsDesc(category, today.atStartOfDay(),
                            today.atTime(23, 59, 59));

            if (!articles.isEmpty()) {
                headArticles.add(articles.get(0));
            }
        }
        return headArticles;
    }

    private Category getHeadLineCategory(){
        return mainArticleRepository.findAllByOrderByArticleViewsDesc().stream()
                .findFirst()
                .map(MainArticle::getCategory)
                .orElse(null);
    }

}
