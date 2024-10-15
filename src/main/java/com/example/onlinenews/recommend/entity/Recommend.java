package com.example.onlinenews.recommend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

//추후 주석 해제
//import com.example.onlinenews.user.entity.User;
//import com.example.onlinenews.article.entity.Article;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recommend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rec_id; // 추천 아이디 (PK)

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    @JsonIgnore
//    private User user_id; // 추후 주석 해제

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "article_id")
//    @JsonIgnore
//    private Article article_id; // 추후 주석 해제

    @Column(nullable = false)
    private Boolean rating; // 추천/비추천

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime timestamp; // 평점한 시각
}
