package com.example.onlinenews.request.api;

import com.example.onlinenews.error.StateResponse;
import com.example.onlinenews.request.dto.RequestCommentDto;
import com.example.onlinenews.request.dto.RequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/request")
@Tag(name = "Request", description = "요청 API")
public interface RequestApi {

    @GetMapping("")
    @Operation(summary = "소속 직원 요청 조회", description = "편집장 밑에 소속된 직원들의 요청을 조회합니다")
    List<RequestDto> getByPublisher(HttpServletRequest request);

    @GetMapping("/{reqId}")
    @Operation(summary = "요청 id로 요청 조회", description = "요청아이디(path)로 해당 요청을 조회합니다.")
    RequestDto read(@PathVariable Long reqId);

    @PostMapping("/{articleId}/convert-private")
    @Operation(summary = "기사 비공개 요청", description = "편집장에게 비공개 요청합니다.")
    RequestDto createPrivateRequest(HttpServletRequest request, @PathVariable Long articleId);

    @PostMapping("/{articleId}/convert-public")
    @Operation(summary = "기사 공개 요청", description = "편집장에게 공개 요청을 합니다.")
    RequestDto createPublicRequest(HttpServletRequest request, @PathVariable Long articleId);

    @PatchMapping("/{reqId}/enroll")
    @Operation(summary = "시민 기자 등록 요청 수락", description = "편집장이 시민 기자 등록 요청을 수락합니다.")
    ResponseEntity<?> enrollRequestAccept(HttpServletRequest request, @PathVariable Long reqId);

    @PatchMapping("/{reqId}/enroll/reject")
    @Operation(summary = "시민 기자 등록 요청 거절", description = "편집장이 시민 기자 등록 요청을 거절합니다.")
    ResponseEntity<?> enrollRequestReject(HttpServletRequest request, @PathVariable Long reqId);

    @PatchMapping("/{reqId}/approve")
    @Operation(summary = "요청 수락", description = "편집장이 id에 해당하는 요청을 수락합니다.")
    ResponseEntity<?> requestAccept(HttpServletRequest request, @PathVariable Long reqId);


    @PutMapping("/{reqId}/hold")
    @Operation(summary = "요청 보류", description = "편집장이 id에 해당하는 요청을 보류하고 커멘트를 남깁니다.")
    ResponseEntity<?> requestHold(HttpServletRequest request, @PathVariable Long reqId, @RequestBody RequestCommentDto requestCommentDto);

    @PutMapping("/{reqId}/reject")
    @Operation(summary = "요청 거절", description = "편집장이 id에 해당하는 요청을 거절하고 커멘트를 남깁니다.")
    ResponseEntity<?> requestReject(HttpServletRequest request, @PathVariable Long reqId, @RequestBody RequestCommentDto requestCommentDto);


    @GetMapping("/status")
    @Operation(summary = "상태별로 요청 조회", description = "상태를 입력(param)하여 해당 상태의 요청들을 조회합니다.")
    List<RequestDto> getByStatus(HttpServletRequest request, @RequestParam String keyword);

}
