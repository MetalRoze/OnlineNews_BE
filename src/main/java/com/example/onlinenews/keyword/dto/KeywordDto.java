package com.example.onlinenews.keyword.dto;

import com.example.onlinenews.keyword.entity.Keyword;
import lombok.Data;

@Data
public class KeywordDto {
    private String user_email;
    private String keyword;

    public KeywordDto(String user_email, String keyword){
        this.user_email = user_email;
        this.keyword = keyword;
    }

    public static KeywordDto fromEntity(Keyword keyword){
        return new KeywordDto(
                keyword.getUser().getEmail(),
                keyword.getKeyword()
        );
    }
}
