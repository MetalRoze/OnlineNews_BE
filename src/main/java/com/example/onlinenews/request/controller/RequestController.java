package com.example.onlinenews.request.controller;

import com.example.onlinenews.request.api.RequestApi;
import com.example.onlinenews.request.dto.RequestDto;
import com.example.onlinenews.request.entity.RequestStatus;
import com.example.onlinenews.request.service.RequestService;
import lombok.RequiredArgsConstructor;
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
    public RequestDto read(Long req_id) {
        return requestService.read(req_id);
    }

    @Override
    public RequestStatus requestAccept(Long req_id) {

        //임시로 userID, 1 (추후 토큰검사)
        return requestService.requestAccept(1L, req_id);
    }

}
