package com.example.onlinenews.like.service;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.article.repository.ArticleRepository;
import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.error.StateResponse;
import com.example.onlinenews.keyword.entity.Keyword;
import com.example.onlinenews.like.dto.ArticleLikeDto;
import com.example.onlinenews.like.entity.ArticleLike;
import com.example.onlinenews.like.repository.ArticleLikeRepository;
import com.example.onlinenews.notification.entity.JournalistNotification;
import com.example.onlinenews.notification.service.NotificationService;
import com.example.onlinenews.user.entity.User;
import com.example.onlinenews.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public long likeCreate(String email, Long articleId){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND));
        Optional<ArticleLike> optionalArticleLike= articleLikeRepository.findByUserAndArticle(user,article);
        if(optionalArticleLike.isPresent()){
            throw new BusinessException(ExceptionCode.ALREADY_LIKED);
        }
        ArticleLike articleLike = ArticleLike.builder()
                .user(user)
                .article(article)
                .createdAt(LocalDateTime.now())
                .build();

        articleLikeRepository.save(articleLike);
        notificationService.createLikeNoti(articleLike);
//        List<Keyword> keywords = article.getKeywords();
//        articleLike.setK
        updateUserKeywords(user, article.getKeywords());

        return articleLike.getId();
    }

    //사용자가 좋아요 한 기사 내역 조회
    public List<ArticleLikeDto> myLikes(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        return articleLikeRepository.findByUser(user).stream()
                .map(ArticleLikeDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ArticleLikeDto> articleLikes(Long articleId){
        Article article = articleRepository.findById(articleId).orElseThrow(()->new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND));
        return articleLikeRepository.findByArticle(article).stream()
                .map(ArticleLikeDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public StateResponse deleteLike(String email, Long articleLikeId){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        ArticleLike articleLike = articleLikeRepository.findById(articleLikeId).orElseThrow(()->new BusinessException(ExceptionCode.ARTICLE_LIKE_NOT_FOUND));
        if(user.getId()!=articleLike.getUser().getId()){
            throw new BusinessException(ExceptionCode.USER_MISMATCH);
        }

        articleLikeRepository.delete(articleLike);
        return StateResponse.builder().code("200").message("좋아요 취소 완료").build();
    }

    public Long checkLike(String email, Long articleId){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND));
        Optional<ArticleLike> optionalArticleLike =articleLikeRepository.findByUserAndArticle(user, article);

//        return optionalArticleLike.isPresent();
        return optionalArticleLike.map(ArticleLike::getId).orElse(null);
    }

    private void updateUserKeywords(User user ,List<String> articleKeywords){
        List<String> customKeywords = user.getCustomKeywords();  // 사용자 키워드 리스트 가져오기

        // 기사에서 가져온 키워드 리스트를 사용자 키워드 리스트에 추가
        for (String keyword : articleKeywords) {
            // 이미 키워드가 사용자 키워드에 없다면 추가
            if (!customKeywords.contains(keyword)) {
                customKeywords.add(keyword);
            }
        }

        // 사용자 키워드 리스트를 업데이트
        userRepository.save(user);  // 사용자의 키워드 리스트 저장
    }

}
