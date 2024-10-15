package com.example.onlinenews.publisher.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pub_id; // 신문사 아이디 (PK)

    @Column(nullable = false)
    private String pub_name; // 신문사 이름

    @Column
    private String pub_url; // 홈페이지 링크

    @Column
    private String pub_img; // 신문사 로고 URL

}
