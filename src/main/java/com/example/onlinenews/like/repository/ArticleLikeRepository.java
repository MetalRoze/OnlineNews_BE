package com.example.onlinenews.like.repository;

import com.example.onlinenews.like.entity.ArticleLike;
import com.example.onlinenews.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
   List< ArticleLike> findByUser(User user);
}
