package com.github.twitch4j.client.websocket;

import com.github.twitch4j.client.websocket.domain.WebsocketConnectionState;
import com.github.twitch4j.common.util.ExponentialBackoffStrategy;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

@Slf4j
public class WebsocketConnection implements AutoCloseable {

    /**
     * connection configuration
     */
    @Getter
    protected final WebsocketConnectionConfig config;

    /**
     * holds the underlying webSocket
     */
    private volatile WebSocket webSocket;

    /**
     * connection state
     */
    @Getter
    private volatile WebsocketConnectionState connectionState = WebsocketConnectionState.DISCONNECTED;

    /**
     * Calls {@link ExponentialBackoffStrategy#reset()} upon a successful websocket connection
     */
    private volatile Future<?> backoffClearer;

    /**
     * WebSocket Factory
     */
    protected final WebSocketFactory webSocketFactory;

    /**
     * WebSocket Adapter
     */
    protected final WebSocketAdapter webSocketAdapter;

    /**
     * Tracks the timestamp of the last outbound ping
     */
    protected final AtomicLong lastPing = new AtomicLong();

    @Getter
    protected volatile long latency = -1L;

    /**
     * TwitchWebsocketConnection
     *
     * @param configSpec the websocket connection configuration
     */
    public WebsocketConnection(Consumer<WebsocketConnectionConfig> configSpec) {
        config = WebsocketConnectionConfig.process(configSpec);

        // webSocketFactory and proxy configuration
        this.webSocketFactory = new WebSocketFactory();
        if (config.proxyHost() != null) {
            webSocketFactory.getProxySettings()
                .setHost(config.proxyHost())
                .setPort(config.proxyPort())
                .setId(config.proxyUsername())
                .setPassword(config.proxyPassword());
        }

        // adapter
        webSocketAdapter = new WebSocketAdapter() {
            @Override
            public void onConnected(WebSocket ws, Map<String, List<String>> headers) {
                // hook: on connected
                config.onConnected().run();

                // Connection Success
                connectionState = WebsocketConnectionState.CONNECTED;
                backoffClearer = config.taskExecutor().schedule(() -> {
                    if (connectionState == WebsocketConnectionState.CONNECTED)
                        config.backoffStrategy().reset();
                }, 30, TimeUnit.SECONDS);
            }

            @Override
            public void onTextMessage(WebSocket ws, String text) {
                // hook: on text message
                config.onTextMessage().accept(text);
            }

            @Override
            public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
                if (!connectionState.equals(WebsocketConnectionState.DISCONNECTING)) {
                    log.info("Connection to WebSocket [{}] lost! Retrying soon ...", config.baseUrl());

                    // connection lost - reconnecting
                    if (backoffClearer != null) backoffClearer.cancel(false);
                    config.taskExecutor().schedule(() -> reconnect(), config.backoffStrategy().get(), TimeUnit.MILLISECONDS);
                } else {
                    connectionState = WebsocketConnectionState.DISCONNECTED;
                    log.info("Disconnected from WebSocket [{}]!", config.baseUrl());
                }
            }

            @Override
            public void onFrameSent(WebSocket websocket, WebSocketFrame frame) {
                if (frame != null && frame.isPingFrame()) {
                    lastPing.compareAndSet(0L, System.currentTimeMillis());
                }
            }

            @Override
            public void onPongFrame(WebSocket websocket, WebSocketFrame frame) {
                final long last = lastPing.getAndSet(0L);
                if (last > 0) {
                    latency = System.currentTimeMillis() - last;
                    log.trace("TwitchChat: Round-trip socket latency recorded at {} ms.", latency);
                }
            }
        };
    }

    /**
     * Connect to the WebSocket
     */
    @Synchronized
    public void connect() {
        if (connectionState.equals(WebsocketConnectionState.DISCONNECTED) || connectionState.equals(WebsocketConnectionState.RECONNECTING)) {
            try {
                // hook: on pre connect
                config.onPreConnect().run();

                // Change Connection State
                connectionState = WebsocketConnectionState.CONNECTING;

                // init websocket
                webSocket = webSocketFactory.createSocket(config.baseUrl());
                webSocket.setPingInterval(config.wsPingPeriod());
                webSocket.clearListeners();
                webSocket.addListener(webSocketAdapter);

                // connect
                this.webSocket.connect();

                // hook: post connect
                config.onPostConnect().run();
            } catch (Exception ex) {
                log.error("connection to webSocket server {} failed: retrying ...", config.baseUrl(), ex);
                // Sleep before trying to reconnect
                try {
                    config.backoffStrategy().sleep();
                } catch (Exception ignored) {

                } finally {
                    // reconnect
                    reconnect();
                }
            }
        }
    }

    /**
     * Disconnect from the WebSocket
     */
    @Synchronized
    public void disconnect() {
        if (connectionState.equals(WebsocketConnectionState.CONNECTED)) {
            // hook: disconnecting
            config.onDisconnecting().run();

            connectionState = WebsocketConnectionState.DISCONNECTING;
        }

        // hook: pre disconnect
        config.onPreDisconnect().run();

        connectionState = WebsocketConnectionState.DISCONNECTED;

        // CleanUp
        this.webSocket.disconnect();
        this.webSocket.clearListeners();
        this.webSocket = null;

        // hook: post disconnect
        config.onPostDisconnect().run();
    }

    /**
     * Reconnecting to the WebSocket
     */
    @Synchronized
    public void reconnect() {
        connectionState = WebsocketConnectionState.RECONNECTING;
        disconnect();
        connect();
    }


    /**
     * sends a message to the websocket server
     *
     * @param message message content
     */
    public boolean sendText(String message) {
        // only send if state is CONNECTING or CONNECTED
        if (!connectionState.equals(WebsocketConnectionState.CONNECTED) && !connectionState.equals(WebsocketConnectionState.CONNECTING)) {
            return false;
        }

        this.webSocket.sendText(message);

        return true;
    }

    @Override
    public void close() throws Exception {
        disconnect();
    }
}
