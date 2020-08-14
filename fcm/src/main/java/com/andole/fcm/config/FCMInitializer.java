package com.andole.fcm.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Getter
@Component
public class FCMInitializer {
    @Value("${fcm.account.path}")
    private String fcmAccountPath;

    @PostConstruct
    public void initialize() throws IOException {
        InputStream accountStream = new ClassPathResource(fcmAccountPath).getInputStream();
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(accountStream))
                .build();
        FirebaseApp.initializeApp(options);
    }
}
