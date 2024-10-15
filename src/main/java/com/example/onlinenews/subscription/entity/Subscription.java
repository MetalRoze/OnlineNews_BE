package com.example.onlinenews.subscription.entity;

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

//    @ManyToOne(fetch = FetchType.LAZY)
//    private User user; // 사용자 (FK)
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
