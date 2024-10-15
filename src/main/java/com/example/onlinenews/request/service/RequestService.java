package com.example.onlinenews.request.service;

import com.example.onlinenews.request.dto.RequestDto;
import com.example.onlinenews.request.entity.Request;
import com.example.onlinenews.request.repository.RequestRepository;
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

    //req_id로 개별 조회
    public RequestDto read(Long req_id){
        Request request = requestRepository.findById(req_id).orElseThrow(() -> new IllegalArgumentException("해당 요청이 없습니다."));
        return RequestDto.fromEntity(request);
    }

}
