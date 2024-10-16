package com.example.onlinenews.user.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class JournalistSignupRequestDTO {
    private String user_name;
    private String user_email;
    private String user_pw;
    private String user_pw2;
    private String user_cp;
    private MultipartFile user_img;
    private String user_sex;
    private String publisher;
}
