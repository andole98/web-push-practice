package com.andole.push_api.service;

import com.andole.push_api.command.PushRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.martijndwars.webpush.*;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.jose4j.lang.JoseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebPushService {
    private final PushService pushService;
    private final Subscribers subscribers;
    private final ObjectMapper objectMapper;

    public String getPublicKey() {
        ECPublicKey publicKey = (ECPublicKey) pushService.getPublicKey();
        return Base64Encoder.encodeUrl(Utils.encode(publicKey));
    }

    public void add(Subscription subscription) {
        subscribers.subscribe(subscription);
    }

    public void remove(Subscription subscription) {
        subscribers.unsubscribe(subscription);
    }

    public void pushAll(PushRequest request) {
        subscribers.findAll().stream()
                .map(subscription -> {
                    try {
                        return new Notification(subscription, objectMapper.writeValueAsString(request));
                    } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException | JsonProcessingException e) {
                        log.error("Notification Validate Error", e);
                        throw new RuntimeException(e.getMessage());
                    }
                })
                .map(notification -> {
                    try {
                        return pushService.send(notification);
                    } catch (GeneralSecurityException | IOException | JoseException | ExecutionException | InterruptedException e) {
                        log.error("Encrypt Error", e);
                        throw new RuntimeException((e.getMessage()));
                    }
                })
                .forEach(httpResponse -> log.info("Status {}", httpResponse.getStatusLine().getStatusCode()));
    }
}
