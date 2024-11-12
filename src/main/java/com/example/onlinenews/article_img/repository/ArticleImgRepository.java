package com.example.onlinenews.article_img.repository;

import com.example.onlinenews.article_img.entity.ArticleImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleImgRepository extends JpaRepository<ArticleImg, Long> {
    void deleteByImgUrl(String imgUrl);
}
