package com.andole.fcm.service;

import com.andole.fcm.model.PushRequest;
import com.andole.fcm.model.Subscription;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PushService {
    private final SubscribeService subscribeService;

    public void push(PushRequest request) throws FirebaseMessagingException {
        FirebaseMessaging messaging = FirebaseMessaging.getInstance();
        WebpushConfig webPush = createWebpush(request);

        List<Message> messages = createMessages(webPush);

        BatchResponse result = messaging.sendAll(messages);
        log.info("SUCCESS {}, FAILURE {}", result.getSuccessCount(), result.getFailureCount());
    }

    private WebpushConfig createWebpush(PushRequest request) {
        return WebpushConfig.builder()
                .setNotification(new WebpushNotification(request.getTitle(), request.getBody()))
                .build();
    }

    private List<Message> createMessages(WebpushConfig webPush) {
        return subscribeService.findAll().stream()
                .map(Subscription::getToken)
                .map(token -> Message.builder()
                        .setToken(token)
                        .setWebpushConfig(webPush)
                        .build())
                .peek(message -> log.info(message.toString()))
                .collect(Collectors.toList());
    }
}
