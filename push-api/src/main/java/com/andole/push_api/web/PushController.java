package com.andole.push_api.web;

import com.andole.push_api.command.PushRequest;
import com.andole.push_api.service.WebPushService;
import lombok.RequiredArgsConstructor;
import nl.martijndwars.webpush.Subscription;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PushController {
    private final WebPushService webPushService;

    @GetMapping("/application-server-key")
    public ResponseEntity<String> publicKey() {
        return ResponseEntity.ok(webPushService.getPublicKey());
    }

    @PostMapping("/subscribe")
    @ResponseStatus(HttpStatus.OK)
    public void subscribe(@RequestBody Subscription subscription) {
        webPushService.add(subscription);
    }

    @PostMapping("/pushAll")
    public void pushAll(@RequestBody PushRequest pushRequest) {
        webPushService.pushAll(pushRequest);
    }

    @DeleteMapping("/subscribe")
    @ResponseStatus(HttpStatus.OK)
    public void unsubscribe(@RequestBody Subscription subscription) {
        webPushService.remove(subscription);
    }
}
