package com.example.onlinenews.user.dto;

import lombok.Data;

@Data
public class GeneralCreateRequestDTO {
    private String user_name;
    private String user_email;
    private String user_pw;
    private String user_cp;
    private String user_img;
    private Boolean user_sex;
    private String user_nickname;
}
