package com.example.onlinenews.article.entity;

import com.example.onlinenews.article_img.entity.ArticleImg;
import com.example.onlinenews.like.entity.UserLike;
import com.example.onlinenews.request.entity.RequestStatus;
import com.example.onlinenews.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column
    private String title;

    @Column
    private String subtitle;

    @Column
    private String content;

    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Column
    private LocalDateTime holdAt;

    @Column
    private LocalDateTime approvedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus state;

    @Column
    private Boolean isPublic;

    @Column(nullable = false)
    private int views = 0;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ArticleImg> images = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserLike> userLikes = new ArrayList<>();

    public void updateStatue(RequestStatus newRequestStatus) {
        this.state= newRequestStatus;
    }
}
