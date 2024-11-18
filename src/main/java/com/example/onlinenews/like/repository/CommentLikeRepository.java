package com.example.onlinenews.like.repository;

import com.example.onlinenews.comment.entity.Comment;
import com.example.onlinenews.like.entity.CommentLike;
import com.example.onlinenews.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    boolean existsByUserAndComment(User user, Comment comment);  // 사용자가 특정 댓글에 좋아요를 눌렀는지 확인
    CommentLike findByUserAndComment(User user, Comment comment);

}
