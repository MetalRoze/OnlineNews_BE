package com.example.onlinenews.user.entity;

import com.example.onlinenews.publisher.entity.Publisher;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.joda.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;

import java.util.concurrent.Flow;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; //사용자 id, PK

    @Column(nullable = false)
    private String pw; //비밀번호

    @Column(nullable = false)
    private boolean sex; //성별

    @Column(nullable = false)
    private String email; //이메일주소

    @Column(nullable = false)
    private String name; //이름

    @Column(nullable = false)
    private String cp; //전화번호

    @Column
    private String img; //프로필 사진

    @Column(nullable = false)
    private int grade; //사용자 등급

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt; //가입일자

    @Column
    private String bio; //자기소개

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pub_id")
    @JsonIgnore
    private Publisher publisher;

    @Column
    private String nickname;

}
