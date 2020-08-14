package com.andole.push_api.config;

import lombok.extern.slf4j.Slf4j;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.cli.handlers.GenerateKeyHandler;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.Security;

@Slf4j
@Configuration
public class AppConfig {

    @Bean
    public PushService pushService() throws GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());
        GenerateKeyHandler handler = new GenerateKeyHandler(null);
        KeyPair keyPair = handler.generateKeyPair();
        return new PushService(keyPair);
    }
}
