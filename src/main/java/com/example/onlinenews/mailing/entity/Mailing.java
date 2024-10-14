package com.example.onlinenews.mailing.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mailing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mailing_id; // 메일링 아이디 (PK)

//    @ManyToOne(fetch = FetchType.LAZY)
//    private User user; //사용자id (fk)

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Article article; //기사 id (fk)

    @Column(nullable = false)
    private LocalDateTime send_at; //전송시간

    @Column(nullable = false)
    private boolean isSent; //전송여부

    @Builder
    public Mailing(LocalDateTime send_at, boolean isSent){
        this.send_at = send_at;
        this.isSent = isSent;
    }
}
