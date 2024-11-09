package com.example.onlinenews.history.entity;

import com.example.onlinenews.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "search_term", nullable = false, length = 255)
    private String searchTerm;

    @Column(name = "search_date", nullable = false)
    private LocalDateTime searchDate;

    @Builder
    public SearchHistory(User user, String searchTerm, LocalDateTime searchDate){
        this.user = user;
        this.searchTerm = searchTerm;
        this.searchDate = searchDate;
    }
}
