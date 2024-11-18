package com.example.onlinenews.subscription.dto;

import com.example.onlinenews.request.dto.RequestDto;
import com.example.onlinenews.request.entity.Request;
import com.example.onlinenews.request.entity.RequestStatus;
import com.example.onlinenews.subscription.entity.Subscription;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubscriptionDto {
    private String user_email;
    private String publisher_name;
    private String publisher_img;
    private Long publisher_id;

    public SubscriptionDto(String user_email, String publisher_name, String publisher_img, Long publisher_id){
        this.user_email = user_email;
        this.publisher_name = publisher_name;
        this.publisher_img = publisher_img;
        this.publisher_id = publisher_id;
    }

    public static SubscriptionDto fromEntity(Subscription subscription){
        return new SubscriptionDto(
                subscription.getUser().getEmail(),
                subscription.getPublisher().getName(),
                subscription.getPublisher().getUrl(),
                subscription.getPublisher().getId()
        );
    }
}
