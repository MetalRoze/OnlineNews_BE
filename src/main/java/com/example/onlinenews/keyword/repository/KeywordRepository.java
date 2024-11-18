package com.example.onlinenews.keyword.repository;

import com.example.onlinenews.history.entity.SearchHistory;
import com.example.onlinenews.keyword.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long>  {

    List<Keyword> findByUserId(Long userId);
}
