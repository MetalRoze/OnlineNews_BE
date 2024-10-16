package com.example.onlinenews.publisher.repository;

import com.example.onlinenews.publisher.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Publisher findByName(String pub_name);
}
