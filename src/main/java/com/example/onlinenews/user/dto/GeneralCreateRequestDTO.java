package com.example.onlinenews.user.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class GeneralCreateRequestDTO {
    private String user_name;
    private String user_email;
    private String user_pw;
    private String user_cp;
    private String user_img;
    private Boolean user_sex;
}
