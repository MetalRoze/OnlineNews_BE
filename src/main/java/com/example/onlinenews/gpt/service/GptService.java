package com.example.onlinenews.gpt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GptService {

    @Value("${openai.api-key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper는 재사용 가능

    public ResponseEntity<String> getResponse(String question) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);


            System.out.println("Content-Type: " + headers.getContentType());
            System.out.println("Authorization: " + headers.getFirst(HttpHeaders.AUTHORIZATION));

            // 요청 JSON 생성
            ObjectNode request = objectMapper.createObjectNode();
            request.put("model", "gpt-3.5-turbo");

            ArrayNode messages = objectMapper.createArrayNode();
            ObjectNode message = objectMapper.createObjectNode();
            message.put("role", "user");
            System.out.println("질문: "+question);
            messages.add(message);
            message.put("content", question);

            request.set("messages", messages);
            request.put("max_tokens", 150);

            // HTTP 요청 생성
            HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

            // OpenAI API 호출
            String url = "https://api.openai.com/v1/chat/completions";
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("GPT API 호출 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
