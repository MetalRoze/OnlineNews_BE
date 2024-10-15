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
    private Long id; // 메일링 아이디 (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    @JsonIgnore
    private Article article;

    @Column(nullable = false)
    private LocalDateTime sendAt; //전송시간

    @Column(nullable = false)
    private boolean isSent; //전송여부

    @Builder
    public Mailing(LocalDateTime send_at, boolean is_sent){
        this.sendAt = send_at;
        this.isSent = is_sent;
    }
}
