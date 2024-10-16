package com.example.onlinenews.user.controller;

import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.publisher.service.PublisherService;
import com.example.onlinenews.user.api.UserAPI;
import com.example.onlinenews.user.dto.GeneralCreateRequestDTO;
import com.example.onlinenews.user.dto.GeneralSignupRequestDTO;
import com.example.onlinenews.user.dto.JournalistSignupRequestDTO;
import com.example.onlinenews.user.dto.JournallistCreateRequestDTO;
import com.example.onlinenews.user.entity.User;
import com.example.onlinenews.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController implements UserAPI {
    private final UserService userService;
    private final PublisherService publisherService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> list() {
        return userService.getAllUser();
    }

    @Override
    public User read(Long id) {
        Optional<User> user =  userService.read(id);
        return user.orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @Override
    public ResponseEntity<?> generalSignup(GeneralSignupRequestDTO requestDTO) {
        if(userService.emailExists(requestDTO.getUser_email())){
            throw new BusinessException(ExceptionCode.EMAIL_CONFLICT);
        }

        if(!requestDTO.getUser_pw().equals(requestDTO.getUser_pw2())){
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

        userService.createGeneralUser(createRequestDTO);
        return ResponseEntity.ok(createRequestDTO);
    }

    @Override
    public ResponseEntity<?> journalistSignup(JournalistSignupRequestDTO requestDTO) {
        if(userService.emailExists(requestDTO.getUser_email())){
            throw new BusinessException(ExceptionCode.EMAIL_CONFLICT);
        }

        if(!requestDTO.getUser_pw().equals(requestDTO.getUser_pw2())){
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
    public Boolean emailCheck(String email){
        return !userService.emailExists(email);
    }
}
