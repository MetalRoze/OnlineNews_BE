package com.example.onlinenews.main_article.repository;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.article.entity.Category;
import com.example.onlinenews.main_article.entity.MainArticle;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MainArticleRepository extends JpaRepository<MainArticle, Long> {
    List<MainArticle> findAllByOrderByArticleViewsDesc();

    List<MainArticle> findByCategoryOrderByArticleViewsDesc(Category category);

    Optional<MainArticle> findByArticle(Article article);

    List<MainArticle> findTop1ByCategoryAndCreatedAtBetweenOrderByArticleViewsDesc(Category category,
                                                                                   LocalDateTime startDate,
                                                                                   LocalDateTime endDate);

}
