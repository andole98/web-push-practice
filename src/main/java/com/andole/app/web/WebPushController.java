package com.andole.app.web;

import com.andole.app.model.PushRequest;
import com.andole.app.service.PushService;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class WebPushController {
    private final PushService pushService;

    @PostMapping("/push")
    public void pushAll(@RequestBody PushRequest pushRequest) throws FirebaseMessagingException {
        pushService.push(pushRequest);
    }
}
