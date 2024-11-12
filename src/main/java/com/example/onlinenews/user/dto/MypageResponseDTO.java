package com.example.onlinenews.user.dto;

import lombok.Data;

@Data
public class MypageResponseDTO {
    private String name;
    private String email;
    private String nickname;
    private String cp;
    private String encodedPw;
    private String sex;
    private String bio;
    private String type;
    private String publisher;
}
