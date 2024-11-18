package com.example.onlinenews.notification.repository;

import com.example.onlinenews.notification.entity.JournalistNotification;
import com.example.onlinenews.like.entity.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JournalistNotificationRepository extends JpaRepository<JournalistNotification, Long> {

    @Query("SELECT jn FROM JournalistNotification jn WHERE jn.articleLike = :articleLike")
    List<JournalistNotification> findByArticleLike(@Param("articleLike") ArticleLike articleLike);
}
