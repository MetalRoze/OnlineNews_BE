package com.example.onlinenews.request.controller;

import com.example.onlinenews.request.api.RequestApi;
import com.example.onlinenews.request.dto.RequestCommentDto;
import com.example.onlinenews.request.dto.RequestDto;
import com.example.onlinenews.request.entity.RequestStatus;
import com.example.onlinenews.request.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RequestController implements RequestApi {
    private final RequestService requestService;
//    private final JwtUtil jwtUtil;
    @Override
    public List<RequestDto> list() {
        return requestService.list();
    }

    @Override
    public RequestDto read(Long reqId) {
        return requestService.read(reqId);
    }

    @Override
    public ResponseEntity<?> requestAccept(Long reqId) {
        //임시로 userID, 3 (추후 토큰검사)
        return ResponseEntity.ok(requestService.requestAccept(2L, reqId));
    }

    @Override
    public ResponseEntity<?> requestHold(Long reqId, RequestCommentDto requestCommentDto) {
        //임시로 userID, 3 (추후 토큰검사)
        return ResponseEntity.ok(requestService.requestHold(2L, reqId, requestCommentDto));
    }

    @Override
    public ResponseEntity<?> requestReject(Long reqId, RequestCommentDto requestCommentDto) {
        return ResponseEntity.ok(requestService.requestReject(2L, reqId, requestCommentDto));
    }

    @Override
    public List<RequestDto> getByStatus(String keyword) {
        return requestService.getByStatus(keyword);
    }

}
