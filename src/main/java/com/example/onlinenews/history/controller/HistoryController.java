package com.example.onlinenews.history.controller;

import com.example.onlinenews.history.api.HistoryAPI;
import com.example.onlinenews.history.dto.HistoryCreateRequestDto;
import com.example.onlinenews.history.dto.HistoryDto;
import com.example.onlinenews.history.service.HistoryService;
import com.example.onlinenews.jwt.provider.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HistoryController implements HistoryAPI {
    private final HistoryService historyService;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public List<HistoryDto> getHistorysByUserId(HttpServletRequest servletRequest) {
        String email = jwtTokenProvider.getAccount(jwtTokenProvider.resolveToken(servletRequest));
        return historyService.getById(email);
    }

    @Override
    public ResponseEntity<?> searchTerm(HttpServletRequest servletRequest, HistoryCreateRequestDto requestDto) {
        String email = jwtTokenProvider.getAccount(jwtTokenProvider.resolveToken(servletRequest));
        return ResponseEntity.ok(historyService.historyCreate(email, requestDto));
    }

    @Override
    public ResponseEntity<?> deleteOneTerm(HttpServletRequest servletRequest, Long id) {
        String email = jwtTokenProvider.getAccount(jwtTokenProvider.resolveToken(servletRequest));
        return ResponseEntity.ok(historyService.deleteOneTerm(email, id));
    }

    @Override
    public ResponseEntity<?> deleteAllTerm(HttpServletRequest servletRequest) {
        String email = jwtTokenProvider.getAccount(jwtTokenProvider.resolveToken(servletRequest));
        return ResponseEntity.ok(historyService.deleteAllTerm(email));
    }
}
