package com.example.onlinenews.publisher.dto;

import com.example.onlinenews.publisher.entity.Publisher;
import com.example.onlinenews.publisher.entity.Type;
import com.example.onlinenews.request.dto.RequestDto;
import com.example.onlinenews.request.entity.Request;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class PublisherDto {
    private final Type publisher_type;
    private String publisher_name;
    private String publisher_url;
    private String publisher_img;
    private Long publisher_id;

    public PublisherDto(Long publisher_id, String publisher_name,String publisher_url, String publisher_img, Type publisher_type) {
        this.publisher_id = publisher_id;
        this.publisher_name = publisher_name;
        this.publisher_url = publisher_url;
        this.publisher_img = publisher_img;
        this.publisher_type = publisher_type;
    }
    public static PublisherDto fromEntity(Publisher publisher) {
        return new PublisherDto(
                publisher.getId(),
                publisher.getName(),
                publisher.getUrl(),
                publisher.getImg(),
                publisher.getType()
        );
    }
}
