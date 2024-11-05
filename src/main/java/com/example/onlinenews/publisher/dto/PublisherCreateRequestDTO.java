package com.example.onlinenews.publisher.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class PublisherCreateRequestDTO {
    private String publisher_name;
    private String publisher_url;
    private String publisher_img;
    private String publisher_type;
}
