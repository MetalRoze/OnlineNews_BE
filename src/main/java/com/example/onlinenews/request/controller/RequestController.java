package com.example.onlinenews.request.controller;

import com.example.onlinenews.jwt.entity.CustomUserDetails;
import com.example.onlinenews.jwt.provider.JwtTokenProvider;
import com.example.onlinenews.request.api.RequestApi;
import com.example.onlinenews.request.dto.RequestCommentDto;
import com.example.onlinenews.request.dto.RequestDto;
import com.example.onlinenews.request.entity.RequestStatus;
import com.example.onlinenews.request.service.RequestService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RequestController implements RequestApi {
    private final RequestService requestService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public List<RequestDto> getByPublisher(HttpServletRequest request) {
        String email = jwtTokenProvider.getAccount(jwtTokenProvider.resolveToken(request));
        return requestService.getRequestsForEditor(email);
    }

    @Override
    public RequestDto read(Long reqId) {
        return requestService.read(reqId);
    }

    @Override
    public ResponseEntity<?> requestAccept(HttpServletRequest request, Long reqId) {
        String email = jwtTokenProvider.getAccount(jwtTokenProvider.resolveToken(request));
        return ResponseEntity.ok(requestService.requestAccept(email, reqId));
    }

    @Override
    public ResponseEntity<?> requestHold(HttpServletRequest request, Long reqId, RequestCommentDto requestCommentDto) {
        String email = jwtTokenProvider.getAccount(jwtTokenProvider.resolveToken(request));
        return ResponseEntity.ok(requestService.requestHold(email, reqId, requestCommentDto));
    }

    @Override
    public ResponseEntity<?> requestReject(HttpServletRequest request, Long reqId, RequestCommentDto requestCommentDto) {
        String email = jwtTokenProvider.getAccount(jwtTokenProvider.resolveToken(request));
        return ResponseEntity.ok(requestService.requestReject(email, reqId, requestCommentDto));
    }

    @Override
    public List<RequestDto> getByStatus(String keyword) {
        return requestService.getByStatus(keyword);
    }

}
