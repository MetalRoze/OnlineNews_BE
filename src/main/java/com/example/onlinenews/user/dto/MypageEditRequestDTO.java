package com.example.onlinenews.user.dto;

import lombok.Data;

@Data
public class MypageEditRequestDTO {
    private String nickname;
    private String cp;
    private String pw;
    private String bio;
}
