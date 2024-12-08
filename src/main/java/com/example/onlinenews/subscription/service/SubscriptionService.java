package com.example.onlinenews.subscription.service;

import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.error.StateResponse;
import com.example.onlinenews.publisher.dto.PublisherDto;
import com.example.onlinenews.publisher.repository.PublisherRepository;
import com.example.onlinenews.publisher.service.PublisherService;
import com.example.onlinenews.subscription.dto.SubscriptionCreateRequestDto;
import com.example.onlinenews.subscription.dto.SubscriptionDto;
import com.example.onlinenews.subscription.entity.Subscription;
import com.example.onlinenews.subscription.repository.SubscriptionRepository;
import com.example.onlinenews.user.entity.User;
import com.example.onlinenews.publisher.entity.Publisher;
import com.example.onlinenews.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService  {
//    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final PublisherRepository publisherRepository;

    public StateResponse subscriptionCreate(String email, SubscriptionCreateRequestDto requestDto){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(!optionalUser.isPresent()){
            throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }
        User user = optionalUser.get();

        Optional<Publisher> optionalPublisher = publisherRepository.findById(requestDto.getPublisherId());
        if(!optionalPublisher.isPresent()){
            throw new BusinessException(ExceptionCode.PUBLISHER_NOT_FOUND);
        }
        Publisher publisher = optionalPublisher.get();

        Subscription subscription = Subscription.builder()
                .user(user)
                .publisher(publisher)
                .createdAt(LocalDateTime.now()).build();
        subscriptionRepository.save(subscription);
        return StateResponse.builder().code("success!!!!!").message("신문사 추가 성공").build();
    }

    public List<SubscriptionDto> getById(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(!optionalUser.isPresent()){
            throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }
        User user = optionalUser.get();

        return subscriptionRepository.findByUserId(user.getId()).stream()
                .map(SubscriptionDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public StateResponse unsubscribe(String email, Long publisherId) {
        // 사용자 조회
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }
        User user = optionalUser.get();

        // 발행사 ID와 사용자로 구독 조회
        Optional<Subscription> optionalSubscription = subscriptionRepository.findByPublisherIdAndUser(publisherId, user);
        if (!optionalSubscription.isPresent()) {
            throw new BusinessException(ExceptionCode.SUB_NOT_FOUND);
        }
        Subscription subscription = optionalSubscription.get();

        // 구독 삭제
        subscriptionRepository.delete(subscription);
        return StateResponse.builder().code("sub delete").message("구독 취소 성공").build();
    }

    public boolean isSubscribed(String email, Long pubId){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(!optionalUser.isPresent()){
            throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }
        User user = optionalUser.get();

        Optional<Publisher> optionalPublisher = publisherRepository.findById(pubId);
        if(!optionalPublisher.isPresent()){
            throw new BusinessException(ExceptionCode.PUBLISHER_NOT_FOUND);
        }
        Publisher publisher = optionalPublisher.get();

        return subscriptionRepository.existsByUserAndPublisher(user, publisher);
    }
}
