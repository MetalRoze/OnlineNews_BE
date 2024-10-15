package com.example.onlinenews.request.repository;

import com.example.onlinenews.request.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository  extends JpaRepository<Request, Long> {
}
