package com.example.onlinenews.article.service;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.article.repository.ArticleRepository;
import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.request.entity.RequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    //편집장 요청 처리 시 수정됨
    public void statusUpdate(Long articleId, RequestStatus newRequestStatus){
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND));
        article.updateStatue(newRequestStatus);
    }

}
