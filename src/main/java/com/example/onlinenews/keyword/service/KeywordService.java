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

    public StateResponse keywordCreate(String email, KeywordCreateRequestDto requestDto) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }
        User user = optionalUser.get();

        // 사용자의 맞춤형 키워드 리스트
        List<String> customKeywords = user.getCustomKeywords();

        // 키워드가 50개 이상인 경우, 새로운 키워드를 추가하기 전에 앞쪽부터 삭
        if (customKeywords.size() >= 15) {
            int deleteCount = 1;  // 새로 추가할 키워드 1개에 대해 삭제할 키워드 수는 1개
            for (int i = 0; i < deleteCount; i++) {
                customKeywords.remove(0);  // 가장 앞에 있는 키워드를 삭제
            }
        }
        // 새로운 키워드 추가
        customKeywords.add(requestDto.getKeyword());

        // 수정된 키워드 리스트를 저장
        user.setCustomKeywords(customKeywords);  // User 엔티티의 키워드 업데이트
        userRepository.save(user);

        return StateResponse.builder().code("키워드 추가").message("키워드 생성").build();
    }
}
