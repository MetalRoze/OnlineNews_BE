package com.example.onlinenews.request.dto;

import com.example.onlinenews.request.entity.Request;
import com.example.onlinenews.request.entity.RequestStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestDto {
    private Long req_id;
    private Long user_id;
    private String user_name;
    private Long article_id;
    private String article_title;
    private LocalDateTime created_at;
    private RequestStatus status;

    public RequestDto(Long req_id, Long user_id, String user_name, Long article_id, String article_title, LocalDateTime created_at, RequestStatus status) {
        this.req_id = req_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.article_id = article_id;
        this.article_title = article_title;
        this.created_at = created_at;
        this.status = status;
    }

    public static RequestDto fromEntity(Request request) {
        return new RequestDto(
                request.getReq_id(),
                request.getUser().getUser_id(),
                request.getUser().getUser_name(),
                request.getArticle().getId(),
                request.getArticle().getTitle(),
                request.getCreated_at(),
                request.getStatus()
        );
    }
}
