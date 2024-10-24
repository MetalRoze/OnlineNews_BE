package com.example.onlinenews.user.dto;

import lombok.Data;

@Data
public class AdminCreateRequestDTO {
    private String id;
    private String password;
    private String inviteCode;
}
