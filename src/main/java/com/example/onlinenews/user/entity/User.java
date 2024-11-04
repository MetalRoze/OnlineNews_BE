package com.example.onlinenews.user.entity;

import com.example.onlinenews.publisher.entity.Publisher;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
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

    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    @Column(nullable = false)
    private UserGrade grade; //사용자 등급

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

    public void setPassword(String encodedPassword) {
        this.pw = encodedPassword;
    }
}
