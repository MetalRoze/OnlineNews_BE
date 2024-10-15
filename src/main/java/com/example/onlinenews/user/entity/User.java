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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(nullable = false)
    private long user_id; //사용자 id, PK

    @Column(nullable = false)
    private String user_pw; //비밀번호

    @Column(nullable = false)
    private boolean user_sex; //성별

    @Column(nullable = false)
    private String user_email; //이메일주소

    @Column(nullable = false)
    private String user_name; //이름

    @Column(nullable = false)
    private String user_cp; //전화번호

    @Column
    private String user_img; //프로필 사진

    @Column(nullable = false)
    private int user_grade; //사용자 등급

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime created_at; //가입일자

    @Column
    private String bio; //자기소개

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pub_id")
    @JsonIgnore
    private Publisher pub_id;

}
