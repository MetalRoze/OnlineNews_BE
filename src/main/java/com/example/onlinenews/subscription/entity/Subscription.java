package com.example.onlinenews.subscription.entity;

import com.example.onlinenews.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sub_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Publisher publisher; // 신문사 (FK)

    @Column(nullable = false)
    private LocalDateTime created_at; //구독 날짜

    @Builder
    public Subscription(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
