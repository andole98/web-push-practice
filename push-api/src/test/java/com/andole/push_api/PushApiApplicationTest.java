package com.andole.push_api;

import nl.martijndwars.webpush.PushService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class PushApiApplicationTest {
    @Autowired
    private ApplicationContext context;

    @Test
    void loadContext() {

    }

    @Test
    void name() {
        PushService bean = context.getBean(PushService.class);
        System.out.println(bean.getPublicKey());
    }
}