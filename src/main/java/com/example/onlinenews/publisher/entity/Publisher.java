package com.example.onlinenews.publisher.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @Column
    private String type; //신문사 종류 @@ 쓰는 사람 있는지 물어보ㅏ야됨 !!
}
