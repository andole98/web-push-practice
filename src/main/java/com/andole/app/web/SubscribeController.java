package com.andole.app.web;

import com.andole.app.config.FCMConfig;
import com.andole.app.model.Subscription;
import com.andole.app.service.SubscribeService;
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
