package com.example.onlinenews.notification.entity;

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
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noti_id; // 알림 아이디 (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Article article; //기사 id (fk)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private boolean is_read;

    @Builder
    public Notification(NotificationType type, LocalDateTime created_at, boolean is_read) {
        this.type = type;
        this.created_at = created_at;
        this.is_read = is_read;
    }
}
