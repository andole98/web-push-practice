package com.andole.fcm.service;

import com.andole.fcm.config.FCMConfig;
import com.andole.fcm.model.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class SubscribeService {
    private final FCMConfig fcmConfig;
    private Set<Subscription> subscribers = new HashSet<>();

    public void add(Subscription subscription) {
        log.info(subscription.toString());
        subscribers.add(subscription);
    }

    public void remove(Subscription subscription) {
        subscribers.remove(subscription);
    }

    public int size() {
        return subscribers.size();
    }

    public Set<Subscription> findAll() {
        return new HashSet<>(subscribers);
    }

    public FCMConfig getConfig() {
        return fcmConfig;
    }
}
