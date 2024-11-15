package com.example.onlinenews.keyword.entity;

import com.example.onlinenews.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.web.WebProperties;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // keyword id (PK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private String keyword;  // 저장할 키워드

    @Builder
    public Keyword(User user, String keyword) {
        this.user = user;
        this.keyword = keyword;
    }

//    public void updateKeywords(String[] keywords){
//        this.keywords = keywords;
//    }
}
