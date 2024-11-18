package com.example.onlinenews.request.repository;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.publisher.entity.Publisher;
import com.example.onlinenews.request.entity.Request;
import com.example.onlinenews.request.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository  extends JpaRepository<Request, Long> {
    List<Request> findByPublisherAndStatus(Publisher publisher, RequestStatus requestStatus);

    Optional<Request> findByArticle(Article article);
    List<Request> findByPublisher(Publisher publisher);
}
