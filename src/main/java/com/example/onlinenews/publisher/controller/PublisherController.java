package com.example.onlinenews.publisher.controller;

import com.example.onlinenews.publisher.api.PublisherAPI;
import com.example.onlinenews.publisher.dto.PublisherCreateRequestDTO;
import com.example.onlinenews.publisher.dto.PublisherDto;
import com.example.onlinenews.publisher.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PublisherController implements PublisherAPI {
    private final PublisherService publisherService;

    @Override
    public List<PublisherDto> list() {
        return publisherService.list();
    }

    @Override
    public List<PublisherDto> getByType(String pub_type) {
        return publisherService.getByTypeList(pub_type);
    }


    @Override
    public ResponseEntity<?> publisherCreate(PublisherCreateRequestDTO requestDTO) {
        return ResponseEntity.ok(publisherService.publisherCreate(requestDTO));
    }


}
