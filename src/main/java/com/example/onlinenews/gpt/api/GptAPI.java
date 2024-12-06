package com.example.onlinenews.gpt.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/api/gpt")
@Tag(name = "GPT", description = "gpt 관련 API")

public interface GptAPI {
    @PostMapping("/ask")
    @Operation(summary = "질문", description = "gpt에게 질문합니다.")
    ResponseEntity<?> askGpt(@RequestBody Map<String, String> payload) ;
}
