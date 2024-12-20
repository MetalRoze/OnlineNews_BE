package com.example.onlinenews.notification.entity;

import com.example.onlinenews.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "notification_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public abstract class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 알림 아이디 (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user; //알림 받는 사람

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private String message;

    @Column(nullable = false)
    private boolean isRead;

    public Notification(User user, NotificationType type, LocalDateTime createdAt, String message, boolean isRead) {
        this.user = user;
        this.type = type;
        this.createdAt = createdAt;
        this.message = message;
        this.isRead = isRead;
    }

    public void updateIsRead(boolean newIsRead){
        this.isRead = newIsRead;
    }
}
