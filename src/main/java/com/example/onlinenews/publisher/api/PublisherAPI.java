package com.example.onlinenews.publisher.api;

import com.example.onlinenews.publisher.dto.PublisherCreateRequestDTO;
import com.example.onlinenews.publisher.dto.PublisherDto;
import com.example.onlinenews.publisher.entity.Publisher;
import com.example.onlinenews.subscription.entity.Subscription;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/publisher")
@Tag(name = "Publisher", description = "신문사 관련 API")
public interface PublisherAPI {
    @GetMapping("")
    @Operation(summary = "전체 신문사 목록 조회", description = "전체 신문사 목록을 조회합니다.")
    List<PublisherDto> list(); //subscriptionDTo 만들기

    @GetMapping("/type")
    @Operation(summary = "신문사 종류 별 목록 조회", description = "신문사 종류 별로 목록을 조회합니다.")
    List<PublisherDto> getByType(@RequestParam String pub_type);

    @PostMapping("/add")
    @Operation(summary = "신문사 추가", description = "신문사를 추가합니다.")
    ResponseEntity<?> publisherCreate(@RequestBody PublisherCreateRequestDTO requestDTO);
}
