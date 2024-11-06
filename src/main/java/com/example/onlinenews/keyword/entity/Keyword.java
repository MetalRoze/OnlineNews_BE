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
    private Long id; // keyword id (PK)ㅂ

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(length = 10)
    private String[] keywords = new String[10];

    @Builder
    public Keyword(User user, String[] keywords){
        this.user = user;
        this.keywords = keywords;
    }

    public void updateKeywords(String[] keywords){
        this.keywords = keywords;
    }
}
