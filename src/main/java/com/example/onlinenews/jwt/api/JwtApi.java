package com.example.onlinenews.jwt.api;

import com.example.onlinenews.jwt.dto.JwtToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/token")
@Tag(name = "token", description = "토큰 관련 API")
public interface JwtApi {
    @PostMapping("/reissue")
    @Operation(summary = "토큰 갱신", description = "refresh token으로 토큰을 재발급합니다.")
    ResponseEntity<JwtToken> reissue(@RequestBody String refreshToken);
}
