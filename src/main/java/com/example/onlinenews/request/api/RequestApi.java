package com.example.onlinenews.request.api;

import com.example.onlinenews.request.dto.RequestCommentDto;
import com.example.onlinenews.request.dto.RequestDto;
import com.example.onlinenews.request.entity.RequestStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/admin/request")
@Tag(name = "Request", description = "요청 API")
public interface RequestApi {

    @GetMapping("")
    @Operation(summary = "전체 요청 조회", description = "전체 요청 목록을 조회하는 메소드입니다.")
    List<RequestDto> list();

    @GetMapping("/{reqId}")
    @Operation(summary = "요청 id로 요청 조회", description = "요청아이디(path)로 해당 요청을 조회합니다.")
    RequestDto read(@PathVariable Long reqId);

    @PatchMapping("/{reqId}/accept")
    @Operation(summary = "요청 id로 요청 조회", description = "요청아이디(path)로 해당 요청을 조회합니다.")
    ResponseEntity<?> requestAccept(@PathVariable Long reqId);


    @PutMapping("/{reqId}/hold")
    @Operation(summary = "요청 id로 요청 조회", description = "요청아이디(path)로 해당 요청을 조회합니다.")
    ResponseEntity<?> requestHold(@PathVariable Long reqId, @RequestBody RequestCommentDto requestCommentDto);
}
