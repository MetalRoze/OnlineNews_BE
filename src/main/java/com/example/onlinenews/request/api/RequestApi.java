package com.example.onlinenews.request.api;

import com.example.onlinenews.request.dto.RequestCommentDto;
import com.example.onlinenews.request.dto.RequestDto;
import com.example.onlinenews.request.entity.RequestStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/admin/request")
@Tag(name = "Request", description = "요청 API")
public interface RequestApi {

    @GetMapping("")
    @Operation(summary = "전체 요청 조회", description = "전체 요청 목록을 조회합니다.")
    List<RequestDto> list();

    @GetMapping("/{reqId}")
    @Operation(summary = "요청 id로 요청 조회", description = "요청아이디(path)로 해당 요청을 조회합니다.")
    RequestDto read(@PathVariable Long reqId);

    @PatchMapping("/{reqId}/accept")
    @Operation(summary = "요청 수락", description = "편집장이 id에 해당하는 요청을 수락합니다.")
    ResponseEntity<?> requestAccept(HttpServletRequest request, @PathVariable Long reqId);


    @PutMapping("/{reqId}/hold")
    @Operation(summary = "요청 보류", description = "편집장이 id에 해당하는 요청을 보류하고 커멘트를 남깁니다.")
    ResponseEntity<?> requestHold(HttpServletRequest request, @PathVariable Long reqId, @RequestBody RequestCommentDto requestCommentDto);

    @PutMapping("/{reqId}/reject")
    @Operation(summary = "요청 id로 요청 조회", description = "편집장이 id에 해당하는 요청을 거절하고 커멘트를 남깁니다.")
    ResponseEntity<?> requestReject(HttpServletRequest request, @PathVariable Long reqId, @RequestBody RequestCommentDto requestCommentDto);

    @GetMapping("/status")
    @Operation(summary = "상태별로 요청 조회", description = "상태를 입력(param)하여 해당 상태의 요청들을 조회합니다.")
    List<RequestDto> getByStatus(@RequestParam String keyword);
}
