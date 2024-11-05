package com.example.onlinenews.article.repository;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.article.entity.Category;
import com.example.onlinenews.request.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    // 카테고리별 조회
    List<Article> findByCategory(Category category);

    // 제목 또는 내용을 포함하는 기사 목록 조회
    List<Article> findByTitleContainingOrContentContaining(String title, String content);

    // 사용자 ID로 기사 목록 조회
    List<Article> findByUserId(Long userId);

    // 공개된 기사만 목록 조회
    List<Article> findByIsPublicTrue();

    // 상태에 따라 기사 목록 조회
    List<Article> findByState(RequestStatus state);
}
