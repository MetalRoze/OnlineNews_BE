package com.example.onlinenews.request.controller;

import com.example.onlinenews.request.api.RequestApi;
import com.example.onlinenews.request.dto.RequestDto;
import com.example.onlinenews.request.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RequestController implements RequestApi {
    private final RequestService requestService;

    @Override
    public ResponseEntity<List<RequestDto>> list() {
        return ResponseEntity.ok(requestService.list());
    }

    @Override
    public ResponseEntity<RequestDto> read(Long req_id) {
        return ResponseEntity.ok(requestService.read(req_id));
    }


}
