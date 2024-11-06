package com.example.onlinenews.request.repository;

import com.example.onlinenews.publisher.entity.Publisher;
import com.example.onlinenews.request.entity.Request;
import com.example.onlinenews.request.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository  extends JpaRepository<Request, Long> {
    List<Request> findByArticleUserPublisherAndStatus(Publisher publisher, RequestStatus requestStatus);

    List<Request> findByArticleUserPublisher(Publisher publisher);
}
