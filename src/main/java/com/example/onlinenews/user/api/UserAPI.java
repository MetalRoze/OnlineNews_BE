package com.example.onlinenews.user.api;

import com.example.onlinenews.jwt.dto.JwtToken;
import com.example.onlinenews.user.dto.AdminCreateRequestDTO;
import com.example.onlinenews.user.dto.EditorCreateRequestDTO;
import com.example.onlinenews.user.dto.GeneralSignupRequestDTO;
import com.example.onlinenews.user.dto.JournalistSignupRequestDTO;
import com.example.onlinenews.user.dto.LoginRequestDto;
import com.example.onlinenews.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @Operation(summary = "시민기자 회원가입", description = "시민기자 회원가입을 수행합니다.")
    ResponseEntity<?> journalistSignup(@RequestBody JournalistSignupRequestDTO requestDTO);

    @PostMapping("/signup/admin")
    @Operation(summary = "시스템 관리자 계정 생성(회원가입)", description = "시스템 관리자의 회원가입을 수행합니다")
    ResponseEntity<?> systemAdminSignup(@RequestBody AdminCreateRequestDTO requestDTO);

    @PostMapping("/signup/editor")
    @Operation(summary = "편집장 계정 생성(회원가입)", description = "편집장의 회원가입을 수행합니다. ")
    ResponseEntity<?> editorSignup(@RequestBody EditorCreateRequestDTO requestDTO);

    @GetMapping("/emailCheck")
    @Operation(summary = "이메일 존재하는지 확인", description = "이메일이 이미 존재하는지 확인합니다. ")
    Boolean emailCheck(@RequestParam String email);

    @PostMapping("/login")
    @Operation(summary = "로그인 메서드", description = "사용자의 아이디, 패스워드를 받아 인증합니다. ")
    ResponseEntity<JwtToken> login(@RequestBody LoginRequestDto requestDto);


}
