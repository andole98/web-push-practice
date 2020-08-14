package com.andole.push_api;

import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Utils;
import nl.martijndwars.webpush.cli.commands.GenerateKeyCommand;
import nl.martijndwars.webpush.cli.handlers.GenerateKeyHandler;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.*;

import static org.assertj.core.api.Assertions.assertThat;

public class PushServiceTest {

    @BeforeEach
    public void addProvider() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    void vapidKeys() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException {
        GenerateKeyHandler handler = new GenerateKeyHandler(new GenerateKeyCommand());
        KeyPair keyPair = handler.generateKeyPair();
        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();

        PushService pushService = new PushService(keyPair);
        assertThat((ECPublicKey) pushService.getPublicKey()).isEqualTo(publicKey);
    }

    @Test
    void validateVapidKeys() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        GenerateKeyHandler handler = new GenerateKeyHandler(null);
        KeyPair keyPair = handler.generateKeyPair();

        assertThat(Utils.verifyKeyPair(keyPair.getPrivate(), keyPair.getPublic())).isTrue();
    }
}
