package com.example.onlinenews.history.api;

import com.example.onlinenews.history.dto.HistoryCreateRequestDto;
import com.example.onlinenews.history.dto.HistoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/history")
@Tag(name = "History", description = "사용자 기록 관련 API")
public interface HistoryAPI {
    @GetMapping("")
    @Operation(summary = "사용자 검색 기록 내역 조회", description = "")
    List<HistoryDto> getHistorysByUserId(HttpServletRequest servletRequest);

    @PostMapping("/search")
    @Operation(summary = "검색 기록 요청", description = "사용자가 검색 기록을 요청합니다.")
    ResponseEntity<?> searchTerm(HttpServletRequest servletRequest, @RequestBody HistoryCreateRequestDto requestDto);


    @DeleteMapping("/delete/{id}") //기록 내역 하나 삭제
    @Operation(summary = "사용자 검색 기록 삭제", description = "사용자가 검색 기록 id로 취소를 요청합니다.")
    ResponseEntity<?> deleteOneTerm(HttpServletRequest servletRequest, @PathVariable Long id);

    @DeleteMapping("/alldelete")
    @Operation(summary = "사용자 검색 기록 전체 삭제", description = "사용자의 검색 기록 모두를 취소 요청합니다.")
    ResponseEntity<?> deleteAllTerm(HttpServletRequest servletRequest);
}
