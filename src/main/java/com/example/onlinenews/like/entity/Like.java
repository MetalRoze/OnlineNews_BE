package com.example.onlinenews.like.entity;

import com.example.onlinenews.article.entity.Article;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // private User user

    @ManyToOne
    private Article article;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime created_at;

}