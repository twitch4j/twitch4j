package com.github.twitch4j.client.websocket;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WebsocketConnectionConfigTest {

    @Test
    public void testFullConfiguration() {
        WebsocketConnection connection = new WebsocketConnection(spec -> {
            spec.baseUrl("https://twitch4j.github.io");
            spec.wsPingPeriod(17_000);
            spec.onPreConnect(() -> System.out.println("on-pre-connect"));
            spec.onPostConnect(() -> System.out.println("on-post-connect"));
            spec.onConnected(() -> System.out.println("on-connected"));
            spec.onTextMessage((text) -> System.out.println("on-text-message"));
            spec.onDisconnecting(() -> System.out.println("on-disconnecting"));
            spec.onPreDisconnect(() -> System.out.println("on-pre-disconnect"));
            spec.onPostDisconnect(() -> System.out.println("on-post-disconnect"));
            spec.proxyHost("localhost:3200");
            spec.proxyPort(3128);
            spec.proxyUsername("admin");
            spec.proxyPassword("default");
        });

        Assertions.assertEquals("https://twitch4j.github.io", connection.config.baseUrl());
        Assertions.assertEquals(17_000, connection.config.wsPingPeriod());
        Assertions.assertNotNull(connection.config.onPreConnect());
        Assertions.assertNotNull(connection.config.onPostConnect());
        Assertions.assertNotNull(connection.config.onConnected());
        Assertions.assertNotNull(connection.config.onTextMessage());
        Assertions.assertNotNull(connection.config.onDisconnecting());
        Assertions.assertNotNull(connection.config.onPreDisconnect());
        Assertions.assertNotNull(connection.config.onPostDisconnect());
        Assertions.assertNotNull(connection.config.backoffStrategy());
        Assertions.assertEquals("localhost:3200", connection.config.proxyHost());
        Assertions.assertEquals(3128, connection.config.proxyPort());
        Assertions.assertEquals("admin", connection.config.proxyUsername());
        Assertions.assertEquals("default", connection.config.proxyPassword());
    }

    @Test
    public void testMinimalConfiguration() {
        WebsocketConnection connection = new WebsocketConnection(spec -> {
            spec.baseUrl("https://twitch4j.github.io");
        });

        Assertions.assertEquals("https://twitch4j.github.io", connection.config.baseUrl());
        Assertions.assertEquals(0, connection.config.wsPingPeriod());
        Assertions.assertNull(connection.config.onPreConnect());
        Assertions.assertNull(connection.config.onPostConnect());
        Assertions.assertNull(connection.config.onConnected());
        Assertions.assertNull(connection.config.onTextMessage());
        Assertions.assertNull(connection.config.onDisconnecting());
        Assertions.assertNull(connection.config.onPreDisconnect());
        Assertions.assertNull(connection.config.onPostDisconnect());
        Assertions.assertNotNull(connection.config.backoffStrategy());
        Assertions.assertNull(connection.config.proxyHost());
        Assertions.assertNull(connection.config.proxyPort());
        Assertions.assertNull(connection.config.proxyUsername());
        Assertions.assertNull(connection.config.proxyPassword());
    }

}
