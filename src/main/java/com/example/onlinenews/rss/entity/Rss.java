package com.example.onlinenews.rss.entity;

import com.example.onlinenews.publisher.entity.Publisher;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Rss {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisherId")
    @JsonIgnore
    private Publisher publisher;

    @Column
    private String economyUrl;

    @Column
    private String politicsUrl;

    @Column
    private String societyUrl;

    @Column
    private String entertainmentUrl;

    @Column
    private String cultureUrl;

    @Column
    private String technologyUrl;

    @Column
    private String opinionUrl;

    @Builder
    public Rss(Publisher publisher, String economyUrl, String politicsUrl, String societyUrl, String entertainmentUrl, String cultureUrl, String technologyUrl, String opinionUrl) {
        this.publisher = publisher;
        this.economyUrl = economyUrl;
        this.politicsUrl = politicsUrl;
        this.societyUrl = societyUrl;
        this.entertainmentUrl = entertainmentUrl;
        this.cultureUrl = cultureUrl;
        this.technologyUrl = technologyUrl;
        this.opinionUrl = opinionUrl;
    }
}
