package com.example.onlinenews.rss.dto;

import lombok.Data;

@Data
public class RssCreateDto {
    private Long pubId;
    private String economyUrl;
    private String politicsUrl;
    private String societyUrl;
    private String entertainmentUrl;
    private String cultureUrl;
    private String technologyUrl;
    private String opinionUrl;
}
