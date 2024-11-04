package com.example.onlinenews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OnlineNewsApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineNewsApplication.class, args);
    }

}
