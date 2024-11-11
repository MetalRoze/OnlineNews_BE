package com.example.onlinenews.rss.repository;

import com.example.onlinenews.publisher.entity.Publisher;
import com.example.onlinenews.rss.entity.Rss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RssRepository extends JpaRepository<Rss, Long> {
    Rss  findByPublisher(Publisher publisher);

}
