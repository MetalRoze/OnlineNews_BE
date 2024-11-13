package com.example.onlinenews.user.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MypageEditRequestDTO {
    private MultipartFile img;
    private String nickname;
    private String cp;
    private String pw;
    private String bio;
}
