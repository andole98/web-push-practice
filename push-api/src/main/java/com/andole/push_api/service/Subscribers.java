package com.andole.push_api.service;

import lombok.extern.slf4j.Slf4j;
import nl.martijndwars.webpush.Subscription;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class Subscribers {
    private Map<String, List<Subscription>> subscribers = new HashMap<>();

    public void subscribe(Subscription subscription) {
        log.info("SUBSCRIBE {}", subscription.endpoint);
        subscribers.putIfAbsent(subscription.endpoint, new ArrayList<>());
        subscribers.get(subscription.endpoint).add(subscription);
    }

    public void unsubscribe(Subscription subscription) {
        log.info("UNSUBSCRIBE {}", subscription.endpoint);
        subscribers.remove(subscription.endpoint);
    }

    public List<Subscription> findAll() {
        return subscribers.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
