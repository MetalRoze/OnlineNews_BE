package com.example.onlinenews.like.entity;

import com.example.onlinenews.comment.entity.Comment;
import com.example.onlinenews.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Comment comment;
}
