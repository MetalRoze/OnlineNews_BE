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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainArticleService {
    private final MainArticleRepository mainArticleRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public StateResponse selectArticle(String email, Long articleId){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        checkEditorPermission(user);

        Article article = articleRepository.findById(articleId).orElseThrow(() -> new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND));

        MainArticle mainArticle = MainArticle.builder()
                .publisher(user.getPublisher())
                .article(article)
                .createdAt(LocalDateTime.now())
                .category(article.getCategory())
                .build();
        mainArticleRepository.save(mainArticle);
        return StateResponse.builder().code("200").message("success").build();
    }

    public List<MainArticleDto> mainArticles(){
        return mainArticleRepository.findAllByOrderByArticleViewsDesc().stream()
                .map(MainArticleDto::fromEntity)
                .collect(Collectors.toList());
    }
    //메인 화면 헤드라인 하나
    public List<MainArticleDto> mainHeadline(){
        return mainArticleRepository.findAllByOrderByArticleViewsDesc().stream()
                .limit(1)
                .map(MainArticleDto::fromEntity)
                .collect(Collectors.toList());
    }

    //메인에 카테고리 별로 2개
    public List<MainArticleDto> mainTwoArticlesByCategory(Category category){
        return mainArticleRepository.findByCategoryOrderByArticleViewsDesc(category).stream()
                .limit(2)
                .map(MainArticleDto::fromEntity)
                .collect(Collectors.toList());
    }
    private void checkEditorPermission(User user) {
        if (user.getGrade().getValue() < UserGrade.EDITOR.getValue()) {
            throw new BusinessException(ExceptionCode.USER_NOT_ALLOWED);
        }
    }

}
