package com.example.onlinenews.request.service;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.request.dto.RequestCommentDto;
import com.example.onlinenews.request.dto.RequestCreateDto;
import com.example.onlinenews.request.dto.RequestDto;
import com.example.onlinenews.request.entity.Request;
import com.example.onlinenews.request.entity.RequestStatus;
import com.example.onlinenews.request.repository.RequestRepository;
import com.example.onlinenews.user.entity.User;
import com.example.onlinenews.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    //전체 조회
    public List<RequestDto> list(){
        return requestRepository.findAll().stream()
                .map(RequestDto::fromEntity)
                .collect(Collectors.toList());
    }

    //request id로 개별 조회
    public RequestDto read(Long reqId){
        Request request = requestRepository.findById(reqId).orElseThrow(() -> new BusinessException(ExceptionCode.REQUEST_NOT_FOUND));
        return RequestDto.fromEntity(request);
    }

    //요청 생성 (article 생성 시 주석 해제)
//    public RequestDto create(RequestCreateDto createDto){
//        User user = userRepository.findById(createDto.getUserId()).orElseThrow(() -> new  BusinessException(ExceptionCode.USER_NOT_FOUND));
//        Article article = articleRepository.findById(createDto.getArticleId()).orElseThrow(() -> new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND));
////
//        Request request  = Request.builder()
//                .user(user)
//                .article(article)
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        requestRepository.save(request);
//        return RequestDto.fromEntity(request);
//    }

    //요청 수락
    @Transactional
    public RequestStatus requestAccept (Long userId, Long reqId){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        if(user.getGrade()<4){
            throw new BusinessException(ExceptionCode.USER_NOT_ALLOWED);
        }
        Request request = requestRepository.findById(reqId).orElseThrow(() -> new BusinessException(ExceptionCode.REQUEST_NOT_FOUND));
        if(request.getStatus().equals(RequestStatus.APPROVED)){
            throw new BusinessException(ExceptionCode.ALREADY_APPROVED);
        }
        request.updateStatus(RequestStatus.APPROVED, null);
        return request.getStatus();
    }

    //보류
    @Transactional
    public RequestStatus requestHold(Long userId, Long reqId,  RequestCommentDto requestCommentDto){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        if(user.getGrade()<4){
            throw new BusinessException(ExceptionCode.USER_NOT_ALLOWED);
        }

        Request request = requestRepository.findById(reqId).orElseThrow(() -> new BusinessException(ExceptionCode.REQUEST_NOT_FOUND));
        if(request.getStatus().equals(RequestStatus.HOLDING)){
            throw new BusinessException(ExceptionCode.ALREADY_HOLDING);
        }
        String comment = requestCommentDto.getComment();
        request.updateStatus(RequestStatus.HOLDING, comment);
        return request.getStatus();
    }

}
