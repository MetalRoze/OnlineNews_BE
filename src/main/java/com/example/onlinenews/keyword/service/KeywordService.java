package com.example.onlinenews.keyword.service;

import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.error.StateResponse;
import com.example.onlinenews.history.dto.HistoryDto;
import com.example.onlinenews.keyword.dto.KeywordCreateRequestDto;
import com.example.onlinenews.keyword.dto.KeywordDto;
import com.example.onlinenews.keyword.entity.Keyword;
import com.example.onlinenews.keyword.repository.KeywordRepository;
import com.example.onlinenews.user.entity.User;
import com.example.onlinenews.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.plaf.nimbus.State;
import java.security.Key;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeywordService {
    private final UserRepository userRepository;
    private final KeywordRepository keywordRepository;

    public List<Keyword> getAllKeyword() {{return keywordRepository.findAll();}}

    public List<KeywordDto> getById(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(!optionalUser.isPresent()){
            throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }
        User user = optionalUser.get();

        return keywordRepository.findByUserId(user.getId()).stream()
                .map(KeywordDto::fromEntity)
                .collect(Collectors.toList());
    }

    public StateResponse keywordCreate(String email, KeywordCreateRequestDto requestDto){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(!optionalUser.isPresent()){
            throw  new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }
        User user = optionalUser.get();

        Keyword keyword = Keyword.builder()
                .user(user)
                .keyword(requestDto.getKeyword()).build();
        keywordRepository.save(keyword);
        return StateResponse.builder().code("키워드 추가").message("키워드 생성").build();
    }
}
