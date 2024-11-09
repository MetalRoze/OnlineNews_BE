package com.example.onlinenews.publisher.entity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 신문사 아이디 (PK)

    @Column(nullable = false)
    private String name; // 신문사 이름

    @Column
    private String url; // 홈페이지 링크

    @Column
    private String img; // 신문사 로고 URL

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type; //신문사 종류
}
