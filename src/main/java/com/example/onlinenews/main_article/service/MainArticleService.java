package com.example.onlinenews.main_article.service;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.article.repository.ArticleRepository;
import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.error.StateResponse;
import com.example.onlinenews.main_article.dto.MainArticleDto;
import com.example.onlinenews.main_article.dto.SelectArticleDto;
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

    public StateResponse selectArticle(String email, SelectArticleDto selectArticleDto){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        checkEditorPermission(user);

        Article article = articleRepository.findById(selectArticleDto.getArticleId()).orElseThrow(() -> new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND));

        MainArticle mainArticle = MainArticle.builder()
                .user(user)
                .article(article)
                .createdAt(LocalDateTime.now())
                .displayOrder(selectArticleDto.getDisplayOrder())
                .build();
        mainArticleRepository.save(mainArticle);
        return StateResponse.builder().code("200").message("success").build();
    }

    public List<MainArticleDto> mainArticleList(){
        return mainArticleRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(MainArticleDto::fromEntity)
                .collect(Collectors.toList());
    }
    private void checkEditorPermission(User user) {
        if (user.getGrade().getValue() < UserGrade.EDITOR.getValue()) {
            throw new BusinessException(ExceptionCode.USER_NOT_ALLOWED);
        }
    }

}
