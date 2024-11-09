package com.example.onlinenews.request.service;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.notification.service.NotificationService;
import com.example.onlinenews.request.dto.RequestCommentDto;
import com.example.onlinenews.request.dto.RequestDto;
import com.example.onlinenews.request.entity.Request;
import com.example.onlinenews.request.entity.RequestStatus;
import com.example.onlinenews.request.repository.RequestRepository;
import com.example.onlinenews.user.entity.User;
import com.example.onlinenews.user.entity.UserGrade;
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
    private final NotificationService notificationService;

    //request id로 개별 조회
    public RequestDto read(Long reqId){
        Request request = requestRepository.findById(reqId).orElseThrow(() -> new BusinessException(ExceptionCode.REQUEST_NOT_FOUND));
        return RequestDto.fromEntity(request);
    }

    //요청 생성
    public void create(User user, Article article){
        Request request  = Request.builder()
                .user(user)
                .article(article)
                .createdAt(LocalDateTime.now())
                .status(RequestStatus.PENDING)
                .build();
        requestRepository.save(request);
        notificationService.createRequestNoti(request);
    }

    //시민 기자 등록 요청
    public void createEnrollRequest(User user, User citizenUser){
        Request request = Request.builder()
                .user(user)
                .citizenUser(citizenUser)
                .createdAt(LocalDateTime.now())
                .status(RequestStatus.PENDING)
                .build();
        requestRepository.save(request);
        notificationService.createEnrollNoti(request);
    }

    //요청 수락
    @Transactional
    public RequestStatus requestAccept (String email, Long reqId){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        checkEditorPermission(user);
        Request request = requestRepository.findById(reqId).orElseThrow(() -> new BusinessException(ExceptionCode.REQUEST_NOT_FOUND));
        if(request.getStatus().equals(RequestStatus.APPROVED)){
            throw new BusinessException(ExceptionCode.ALREADY_APPROVED);
        }
        request.updateStatus(RequestStatus.APPROVED, null);

        //기사 상태 업데이트
        request.getArticle().updateStatue(request.getStatus());

        //승인 알림
        notificationService.createApprovedNoti(request);
        return request.getStatus();
    }

    //보류
    @Transactional
    public RequestStatus requestHold(String email, Long reqId, RequestCommentDto requestCommentDto){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        checkEditorPermission(user);
        Request request = requestRepository.findById(reqId).orElseThrow(() -> new BusinessException(ExceptionCode.REQUEST_NOT_FOUND));
        if(request.getStatus().equals(RequestStatus.HOLDING)){
            throw new BusinessException(ExceptionCode.ALREADY_HOLDING);
        }
        String comment = requestCommentDto.getComment();
        request.updateStatus(RequestStatus.HOLDING, comment);

        request.getArticle().updateStatue(request.getStatus());
        //보류 알림
        notificationService.createHeldNoti(request);
        return request.getStatus();
    }

    //거절
    @Transactional
    public RequestStatus requestReject(String email, Long reqId, RequestCommentDto requestCommentDto){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        checkEditorPermission(user);
        Request request = requestRepository.findById(reqId).orElseThrow(() -> new BusinessException(ExceptionCode.REQUEST_NOT_FOUND));
        if(request.getStatus().equals(RequestStatus.REJECTED)){
            throw new BusinessException(ExceptionCode.ALREADY_REJECTED);
        }
        String comment = requestCommentDto.getComment();
        request.updateStatus(RequestStatus.REJECTED, comment);

        request.getArticle().updateStatue(request.getStatus());

        //거절 알림
        notificationService.createRejectedNoti(request);
        return request.getStatus();
    }

    //상태로 요청조회
    @Transactional
    public List<RequestDto> getByStatus(String email,String keyword) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        checkEditorPermission(user);
        RequestStatus enumStatus = getRequestStatusFromKeyword(keyword);
        return requestRepository.findByArticleUserPublisherAndStatus(user.getPublisher(),enumStatus).stream()
                .map(RequestDto::fromEntity)
                .collect(Collectors.toList());
    }

    //편집장의 출판사 소속 직원들의 요청 확인
    public List<RequestDto> getRequestsForEditor(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        checkEditorPermission(user);

        return requestRepository.findByArticleUserPublisher(user.getPublisher()).stream()
                .map(RequestDto::fromEntity)
                .collect(Collectors.toList());
    }

    //시민기자 등록 요청


    private void checkEditorPermission(User user) {
        if (user.getGrade().getValue() < UserGrade.EDITOR.getValue()) {
            throw new BusinessException(ExceptionCode.USER_NOT_ALLOWED);
        }
    }

    private RequestStatus getRequestStatusFromKeyword(String keyword) {
        return switch (keyword.toLowerCase()) {
            case "pending" -> RequestStatus.PENDING;
            case "approved" -> RequestStatus.APPROVED;
            case "holding" -> RequestStatus.HOLDING;
            case "rejected" -> RequestStatus.REJECTED;
            default -> throw new BusinessException(ExceptionCode.NOT_VALID_ERROR);
        };
    }



}
