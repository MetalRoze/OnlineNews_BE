package com.example.onlinenews.request.service;

import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.request.dto.RequestDto;
import com.example.onlinenews.request.entity.Request;
import com.example.onlinenews.request.entity.RequestStatus;
import com.example.onlinenews.request.repository.RequestRepository;
import com.example.onlinenews.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;

    //전체 조회
    public List<RequestDto> list(){
        return requestRepository.findAll().stream()
                .map(RequestDto::fromEntity)
                .collect(Collectors.toList());
    }

    //request id로 개별 조회
    public RequestDto read(Long req_id){
        Request request = requestRepository.findById(req_id).orElseThrow(() -> new BusinessException(ExceptionCode.REQUEST_NOT_FOUND));
        return RequestDto.fromEntity(request);
    }

    //요청 수락
    public RequestStatus requestAccept (Long userId, Long reqId){
//        추후 주석해제
//        User user = userRepository.findById(user_id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
//        if(user.getUser_grade()<9){
//            throw new BusinessException(ExceptionCode.USER_NOT_ALLOWED);
//        }
        Request request = requestRepository.findById(reqId).orElseThrow(() -> new BusinessException(ExceptionCode.REQUEST_NOT_FOUND));
        request.updateStatus(RequestStatus.APPROVED, null);
        return request.getStatus();
    }

}
