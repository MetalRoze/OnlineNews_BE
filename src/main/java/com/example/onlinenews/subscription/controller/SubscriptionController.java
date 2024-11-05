package com.example.onlinenews.subscription.controller;

import com.example.onlinenews.subscription.api.SubscriptionAPI;
import com.example.onlinenews.subscription.dto.SubscriptionAddRequestDto;
import com.example.onlinenews.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SubscriptionController implements SubscriptionAPI {
    private final SubscriptionService subscriptionSerive;

//    @Override
//    public List<String> getSubscriptionsByUserId(Long userId) {
//        return null;
//    }
//
//    @Override
//    public boolean isSubscribed(Long userId, Long newspaperId) {
//        return false;
//    }

//    @Override
//    public ResponseEntity<?> subscribe(SubscriptionAddRequestDto requestDto) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<?> unsubscribe(Long userId, Long newspaperId) {
//        return null;
//    }
}
