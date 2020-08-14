package com.andole.fcm.web;

import com.andole.fcm.config.FCMConfig;
import com.andole.fcm.model.Subscription;
import com.andole.fcm.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequiredArgsConstructor
@RestController
public class SubscribeController {
    private final SubscribeService subscribeService;

    @PostMapping("/subscribe")
    @ResponseStatus(HttpStatus.CREATED)
    public void subscribe(@RequestBody Subscription subscription) {
        subscribeService.add(subscription);
    }

    @GetMapping("/subs-config")
    public ResponseEntity<FCMConfig> config() {
        return ResponseEntity.ok(subscribeService.getConfig());
    }

    @GetMapping("/subscribers")
    public ResponseEntity<Set<Subscription>> findAll() {
        return ResponseEntity.ok(subscribeService.findAll());
    }

    @DeleteMapping("/subscribe")
    @ResponseStatus(HttpStatus.OK)
    public void unSubscribe(@RequestBody Subscription subscription) {
        subscribeService.remove(subscription);
    }
}
