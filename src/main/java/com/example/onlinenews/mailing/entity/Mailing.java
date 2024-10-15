package com.example.onlinenews.mailing.entity;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Mailing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mailing_id; // 메일링 아이디 (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    @JsonIgnore
    private Article article;

    @Column(nullable = false)
    private LocalDateTime send_at; //전송시간

    @Column(nullable = false)
    private boolean is_sent; //전송여부

    @Builder
    public Mailing(LocalDateTime send_at, boolean is_sent){
        this.send_at = send_at;
        this.is_sent = is_sent;
    }
}
