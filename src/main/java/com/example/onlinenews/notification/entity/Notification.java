package com.example.onlinenews.notification.entity;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 알림 아이디 (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    @JsonIgnore
    private Article article;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean isRead;

    @Builder
    public Notification(NotificationType type, LocalDateTime createdAt, boolean isRead) {
        this.type = type;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }
}
