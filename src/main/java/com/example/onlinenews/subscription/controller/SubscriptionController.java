package com.example.onlinenews.subscription.controller;

import com.example.onlinenews.jwt.provider.JwtTokenProvider;
import com.example.onlinenews.subscription.api.SubscriptionAPI;
import com.example.onlinenews.subscription.dto.SubscriptionCreateRequestDto;
import com.example.onlinenews.subscription.dto.SubscriptionDto;
import com.example.onlinenews.subscription.service.SubscriptionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SubscriptionController implements SubscriptionAPI {
    private final SubscriptionService subscriptionService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public List<SubscriptionDto> getSubscriptionsByUserId(HttpServletRequest request) {
        String email = jwtTokenProvider.getAccount(jwtTokenProvider.resolveToken(request));
        return subscriptionService.getById(email);
    }

    @Override
    public boolean isSubscribed(HttpServletRequest request, Long pub_id) {
        String email = jwtTokenProvider.getAccount(jwtTokenProvider.resolveToken(request));
        return subscriptionService.isSubscribed(email, pub_id);
    }

    @Override
    public ResponseEntity<?> subscribeCreate(HttpServletRequest request, SubscriptionCreateRequestDto requestDto) {
        String email = jwtTokenProvider.getAccount(jwtTokenProvider.resolveToken(request));
        return ResponseEntity.ok(subscriptionService.subscriptionCreate(email, requestDto));

    }

    @Override
    public ResponseEntity<?> unsubscribe(HttpServletRequest request, Long id) {
        String email = jwtTokenProvider.getAccount(jwtTokenProvider.resolveToken(request));
        return ResponseEntity.ok(subscriptionService.unsubscribe(email, id));
    }

}
