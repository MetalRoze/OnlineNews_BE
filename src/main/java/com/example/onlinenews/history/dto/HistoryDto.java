package com.example.onlinenews.history.dto;

import com.example.onlinenews.history.entity.SearchHistory;
import lombok.Data;

@Data
public class HistoryDto {
    private String user_email;
    private  String searchTerm;
    private Long id;

    public HistoryDto(String user_email, String searchTerm, Long id){
        this.user_email = user_email;
        this.searchTerm = searchTerm;
        this.id = id;
    }

    public static HistoryDto fromEntity(SearchHistory searchHistory){
        return new HistoryDto(
                searchHistory.getUser().getEmail(),
                searchHistory.getSearchTerm(),
                searchHistory.getId()
        );
    }
}
