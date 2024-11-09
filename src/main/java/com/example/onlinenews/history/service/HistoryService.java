package com.example.onlinenews.history.service;

import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.error.StateResponse;
import com.example.onlinenews.history.dto.HistoryCreateRequestDto;
import com.example.onlinenews.history.dto.HistoryDto;
import com.example.onlinenews.history.entity.SearchHistory;
import com.example.onlinenews.history.repository.HistoryRepository;
import com.example.onlinenews.user.entity.User;
import com.example.onlinenews.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;

    public List<SearchHistory> getAllHistory() {{return historyRepository.findAll();}}

    public List<HistoryDto> getById(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }
        User user = optionalUser.get();

        return historyRepository.findByUserId(user.getId()).stream()
                .map(HistoryDto::fromEntity)
                .collect(Collectors.toList());
    }
    public StateResponse historyCreate(String email, HistoryCreateRequestDto requestDto){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(!optionalUser.isPresent()){
            throw  new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }
        User user = optionalUser.get();

        SearchHistory searchHistory = SearchHistory.builder()
                .user(user)
                .searchTerm(requestDto.getSearchTerm())
                .searchDate(LocalDateTime.now()).build();
        historyRepository.save(searchHistory);
        return StateResponse.builder().code("검색 기록 추가").message("success").build();

        // 이때 똑같은 검새어 검색한 기록 있으면 추가하지 않고 맨처음으로 date 만 갱신하는 걸로???
    }

    @Transactional
    public StateResponse deleteOneTerm(String email, Long id){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(!optionalUser.isPresent()){
            throw  new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }
        User user = optionalUser.get();

        Optional<SearchHistory> optionalSearchHistory = historyRepository.findById(id);
        if (!optionalSearchHistory.isPresent()){
            throw  new BusinessException(ExceptionCode.HISTORY_NOT_FOUND);
        }
        SearchHistory searchHistory = optionalSearchHistory.get();

        // 검색 기록이 현재 사용자와 일치하는지 확인.. 해야되나?
        if(!searchHistory.getUser().equals(user)){
            throw new BusinessException(ExceptionCode.HIS_NOT_MATCH_USER);
        }

        historyRepository.delete(searchHistory);
        return StateResponse.builder().code("검새 기록 삭제").message("success").build();
    }

    @Transactional
    public StateResponse deleteAllTerm(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }
        User user = optionalUser.get();

        // 사용자의 모든 검색 기록 삭제
        historyRepository.deleteByUser(user);

        return StateResponse.builder().code("검색 기록 전체 삭제").message("success").build();
    }
}
