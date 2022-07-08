package com.github.twitch4j.client.websocket;

import com.github.twitch4j.common.config.ProxyConfig;
import com.neovisionaries.ws.client.WebSocket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class WebsocketConnectionConfigTest {

    @Test
    public void testFullConfiguration() throws IOException {
        WebsocketConnection connection = new WebsocketConnection(spec -> {
            spec.baseUrl("https://twitch4j.github.io");
            spec.wsPingPeriod(17_000);
            spec.onPreConnect(() -> System.out.println("on-pre-connect"));
            spec.onPostConnect(() -> System.out.println("on-post-connect"));
            spec.onConnected(() -> System.out.println("on-connected"));
            spec.onStateChanged((oldState, newState) -> System.out.println("on-state-change"));
            spec.onTextMessage((text) -> System.out.println("on-text-message"));
            spec.onDisconnecting(() -> System.out.println("on-disconnecting"));
            spec.onPreDisconnect(() -> System.out.println("on-pre-disconnect"));
            spec.onPostDisconnect(() -> System.out.println("on-post-disconnect"));
            spec.proxyConfig(ProxyConfig.builder().hostname("localhost").port(3128).username("admin").password("default".toCharArray()).build());
            spec.taskExecutor(new ScheduledThreadPoolExecutor(2));
            Map<String, String> headers = new HashMap<>();
            headers.put("User-Agent", "my-user-agent");
            spec.headers(headers);
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
        Assertions.assertEquals("localhost", connection.config.proxyConfig().getHostname());
        Assertions.assertEquals(3128, connection.config.proxyConfig().getPort());
        Assertions.assertEquals("admin", connection.config.proxyConfig().getUsername());
        Assertions.assertEquals("default", String.valueOf(connection.config.proxyConfig().getPassword()));
        Assertions.assertNotNull(connection.config.taskExecutor());
        Assertions.assertEquals("my-user-agent", connection.config.headers().get("User-Agent"));

        // create websocket and check the result
        WebSocket ws = connection.createWebsocket();
        Assertions.assertEquals(17_000, ws.getPingInterval());
    }

    @Test
    public void testMinimalConfiguration() {
        WebsocketConnection connection = new WebsocketConnection(spec -> {
            spec.baseUrl("https://twitch4j.github.io");
        });

        Assertions.assertEquals("https://twitch4j.github.io", connection.config.baseUrl());
        Assertions.assertEquals(0, connection.config.wsPingPeriod());
        Assertions.assertNotNull(connection.config.onPreConnect());
        Assertions.assertNotNull(connection.config.onPostConnect());
        Assertions.assertNotNull(connection.config.onConnected());
        Assertions.assertNotNull(connection.config.onTextMessage());
        Assertions.assertNotNull(connection.config.onDisconnecting());
        Assertions.assertNotNull(connection.config.onPreDisconnect());
        Assertions.assertNotNull(connection.config.onPostDisconnect());
        Assertions.assertNotNull(connection.config.backoffStrategy());
        Assertions.assertNull(connection.config.proxyConfig());
        Assertions.assertNotNull(connection.config.taskExecutor());
        Assertions.assertNull(connection.config.headers());
    }

}
