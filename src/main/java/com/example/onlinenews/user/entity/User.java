package com.example.onlinenews.user.entity;

import com.example.onlinenews.keyword.entity.Keyword;
import com.example.onlinenews.publisher.entity.Publisher;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;

import java.util.ArrayList;
import java.util.List;

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

    // 사용자의 맞춤형 키워드 리스트
    @ElementCollection
    @CollectionTable(name = "user_keywords", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "keyword")
    private List<String> customKeywords = new ArrayList<>();

    public void updatePassword(String encodedPassword) {
        this.pw = encodedPassword;
    }

    public void updateBio(String bio) {
        this.bio = bio;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateCp(String cp) {
        this.cp = cp;
    }

    public void updatePublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public void updateImg(String img) {
        this.img = img;
    }

    public String getSex() {
        if (sex) {
            return "남성";
        } else {
            return "여성";
        }
    }

    public void updateCustomKeywords(List<String> newKeywords){
        this.customKeywords.clear();
        this.customKeywords.addAll(newKeywords);
    }
}
