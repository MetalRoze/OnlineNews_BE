package com.example.onlinenews.user.dto;

import lombok.Data;

@Data
public class EditorCreateRequestDTO {
    private String id;
    private String password;
    private String inviteCode;
    private String publisherName;
}
