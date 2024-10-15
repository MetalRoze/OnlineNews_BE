package com.example.onlinenews.user.api;

import com.example.onlinenews.user.dto.GeneralSignupRequestDTO;
import com.example.onlinenews.user.dto.JournalistSignupRequestDTO;
import com.example.onlinenews.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/user")
@Tag(name = "User", description = "사용자 관련 API")
public interface UserAPI {
    @GetMapping("")
    @Operation(summary = "전체 사용자 목록 조회", description = "전체 사용자 목록을 보여줍니다. ")
    List<User> list();

    @GetMapping("/{id}")
    @Operation(summary = "사용자 상세 조회 ", description = "해당 id를 가진 사용자의 상세 정보를 조회합니다.")
    User read(@PathVariable Long id);

    @PostMapping("/signup/general")
    @Operation(summary = "일반회원 회원가입", description = "일반회원 회원가입을 수행합니다.")
    ResponseEntity<?> generalSignup(@RequestBody GeneralSignupRequestDTO requestDTO);

    @PostMapping("/signup/journalist")
    @Operation(summary = "시민기자 회원가입",description = "시민기자 회원가입을 수행합니다.")
    ResponseEntity<?> journalistSignup(@RequestBody JournalistSignupRequestDTO requestDTO);

    @GetMapping("/emailCheck")
    @Operation(summary = "이메일 존재하는지 확인", description = "이메일이 이미 존재하는지 확인합니다. ")
    Boolean emailCheck(@RequestParam String email);

}