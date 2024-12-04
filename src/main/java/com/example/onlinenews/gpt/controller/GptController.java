package com.example.onlinenews.gpt.controller;
import com.example.onlinenews.gpt.api.GptAPI;
import com.example.onlinenews.gpt.service.GptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class GptController implements GptAPI {

    private final GptService gptService;
    public ResponseEntity<String> askGpt(@RequestBody Map<String, String> payload) {
        String question = payload.get("question");
        return gptService.getResponse(question);
    }
}
