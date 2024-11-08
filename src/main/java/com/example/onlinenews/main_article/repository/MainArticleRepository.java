package com.example.onlinenews.main_article.repository;

import com.example.onlinenews.main_article.entity.MainArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MainArticleRepository extends JpaRepository<MainArticle, Long> {
}
