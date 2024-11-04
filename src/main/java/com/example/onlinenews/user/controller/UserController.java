package com.example.onlinenews.user.controller;

import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.jwt.dto.JwtToken;
import com.example.onlinenews.user.api.UserAPI;
import com.example.onlinenews.user.dto.AdminCreateRequestDTO;
import com.example.onlinenews.user.dto.EditorCreateRequestDTO;
import com.example.onlinenews.user.dto.FindIdRequestDTO;
import com.example.onlinenews.user.dto.FindPwRequestDTO;
import com.example.onlinenews.user.dto.GeneralCreateRequestDTO;
import com.example.onlinenews.user.dto.GeneralSignupRequestDTO;
import com.example.onlinenews.user.dto.JournalistSignupRequestDTO;
import com.example.onlinenews.user.dto.JournallistCreateRequestDTO;
import com.example.onlinenews.user.dto.LoginRequestDTO;
import com.example.onlinenews.user.entity.User;
import com.example.onlinenews.user.entity.UserGrade;
import com.example.onlinenews.user.service.UserService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController implements UserAPI {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> list() {
        return userService.getAllUser();
    }

    @Override
    public User read(Long id) {
        Optional<User> user = userService.read(id);
        return user.orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @Override
    public ResponseEntity<?> generalSignup(GeneralSignupRequestDTO requestDTO) {
        if (userService.emailExists(requestDTO.getUser_email())) {
            throw new BusinessException(ExceptionCode.EMAIL_CONFLICT);
        }

        if (!requestDTO.getUser_pw().equals(requestDTO.getUser_pw2())) {
            throw new BusinessException(ExceptionCode.PASSWORD_MISMATCH);
        }

        // 이미지 처리
        String user_img = null;
        if (requestDTO.getUser_img() != null) {
            user_img = userService.saveProfileImg(requestDTO.getUser_img());
        }

        String user_pw = passwordEncoder.encode(requestDTO.getUser_pw());
        boolean user_sex = !requestDTO.getUser_sex().equals("F");

        GeneralCreateRequestDTO createRequestDTO = new GeneralCreateRequestDTO();
        createRequestDTO.setUser_email(requestDTO.getUser_email());
        createRequestDTO.setUser_pw(user_pw);
        createRequestDTO.setUser_name(requestDTO.getUser_name());
        createRequestDTO.setUser_cp(requestDTO.getUser_cp());
        createRequestDTO.setUser_sex(user_sex);
        createRequestDTO.setUser_img(user_img);
        createRequestDTO.setUser_nickname(requestDTO.getUser_nickname());

        userService.createGeneralUser(createRequestDTO);
        return ResponseEntity.ok(createRequestDTO);
    }

    @Override
    public ResponseEntity<?> journalistSignup(JournalistSignupRequestDTO requestDTO) {
        if (userService.emailExists(requestDTO.getUser_email())) {
            throw new BusinessException(ExceptionCode.EMAIL_CONFLICT);
        }

        if (!requestDTO.getUser_pw().equals(requestDTO.getUser_pw2())) {
            throw new BusinessException(ExceptionCode.PASSWORD_MISMATCH);
        }

        // 이미지 처리
        String user_img = null;
        if (requestDTO.getUser_img() != null) {
            user_img = userService.saveProfileImg(requestDTO.getUser_img());
        }

        String user_pw = passwordEncoder.encode(requestDTO.getUser_pw());
        boolean user_sex = !requestDTO.getUser_sex().equals("F");

        JournallistCreateRequestDTO createRequestDTO = new JournallistCreateRequestDTO();
        createRequestDTO.setUser_email(requestDTO.getUser_email());
        createRequestDTO.setUser_pw(user_pw);
        createRequestDTO.setUser_name(requestDTO.getUser_name());
        createRequestDTO.setUser_cp(requestDTO.getUser_cp());
        createRequestDTO.setUser_sex(user_sex);
        createRequestDTO.setUser_img(user_img);
        createRequestDTO.setPublisher(requestDTO.getPublisher());

        userService.createJournalistUser(createRequestDTO);
        return ResponseEntity.ok(createRequestDTO);
    }

    @Override
    public ResponseEntity<?> systemAdminSignup(AdminCreateRequestDTO requestDTO) {
        String id = requestDTO.getId();
        String pw = requestDTO.getPassword();
        String inviteCode = requestDTO.getInviteCode();

        UserGrade GRADE = UserGrade.SYSTEM_ADMIN;
        if (userService.checkSecreteKey(GRADE, inviteCode)) {
            userService.createAdminUser(id, pw, GRADE, "");
            return ResponseEntity.ok("ADMIN 계정 생성이 완료되었습니다");
        }
        throw new BusinessException(ExceptionCode.INVITE_CODE_MISMATCH);
    }

    @Override
    public ResponseEntity<?> editorSignup(EditorCreateRequestDTO requestDTO) {
        String id = requestDTO.getId();
        String pw = requestDTO.getPassword();
        String inviteCode = requestDTO.getInviteCode();
        String publisherName = requestDTO.getPublisherName();

        UserGrade GRADE = UserGrade.EDITOR;
        if (userService.checkSecreteKey(GRADE, inviteCode)) {
            userService.createAdminUser(id, pw, GRADE, publisherName);
            return ResponseEntity.ok("EDITOR 계정 생성이 완료되었습니다");
        }
        throw new BusinessException(ExceptionCode.INVITE_CODE_MISMATCH);
    }


    @Override
    public Boolean emailCheck(String email) {
        return !userService.emailExists(email);
    }

    @Override
    public ResponseEntity<JwtToken> login(LoginRequestDTO requestDto) {
        return new ResponseEntity<>(userService.login(requestDto), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> findId(FindIdRequestDTO requestDTO) {
        return new ResponseEntity<>(userService.getEmailWithUsernameAndCp(requestDTO), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> findPassword(FindPwRequestDTO requestDTO) {
        return new ResponseEntity<>(userService.generateTemporaryPassword(requestDTO), HttpStatus.OK);
    }
}
