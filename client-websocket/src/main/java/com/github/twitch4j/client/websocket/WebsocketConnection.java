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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
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
    private final AtomicReference<WebsocketConnectionState> connectionState = new AtomicReference<>(WebsocketConnectionState.DISCONNECTED);

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
        if (config.proxyConfig() != null) {
            webSocketFactory.getProxySettings()
                .setHost(config.proxyConfig().getHostname())
                .setPort(config.proxyConfig().getPort())
                .setId(config.proxyConfig().getUsername())
                .setPassword(config.proxyConfig().getPassword() == null ? null : String.valueOf(config.proxyConfig().getPassword()));
        }

        // adapter
        webSocketAdapter = new WebSocketAdapter() {
            @Override
            public void onConnected(WebSocket ws, Map<String, List<String>> headers) {
                // hook: on connected
                config.onConnected().run();

                // Connection Success
                setState(WebsocketConnectionState.CONNECTED);
                backoffClearer = config.taskExecutor().schedule(() -> {
                    if (connectionState.get() == WebsocketConnectionState.CONNECTED)
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
                if (connectionState.get() != WebsocketConnectionState.DISCONNECTING) {
                    setState(WebsocketConnectionState.LOST);
                    log.info("Connection to WebSocket [{}] lost! Retrying soon ...", config.baseUrl());

                    // connection lost - reconnecting
                    if (backoffClearer != null) backoffClearer.cancel(false);
                    long reconnectDelay = config.backoffStrategy().get();
                    if (reconnectDelay < 0) {
                        log.debug("Maximum retry count for websocket reconnection attempts was hit.");
                        config.backoffStrategy().reset(); // start fresh on the next manual connect() call
                    } else {
                        config.taskExecutor().schedule(() -> reconnect(), reconnectDelay, TimeUnit.MILLISECONDS);
                    }
                } else {
                    setState(WebsocketConnectionState.DISCONNECTED);
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
                    log.trace("T4J Websocket: Round-trip socket latency recorded at {} ms.", latency);
                }
            }
        };
    }

    protected WebSocket createWebsocket() throws IOException {
        WebSocket ws = webSocketFactory.createSocket(config.baseUrl());
        ws.setPingInterval(config.wsPingPeriod());
        if (config.headers() != null)
            config.headers().forEach(ws::addHeader);
        ws.clearListeners();
        ws.addListener(webSocketAdapter);

        return ws;
    }

    protected void setState(WebsocketConnectionState newState) {
        WebsocketConnectionState oldState = connectionState.getAndSet(newState);
        if (oldState != newState) {
            config.onStateChanged().accept(oldState, newState);
        }
    }

    /**
     * Connect to the WebSocket
     */
    @Synchronized
    public void connect() {
        WebsocketConnectionState connectionState = this.connectionState.get();
        if (connectionState.equals(WebsocketConnectionState.DISCONNECTED) || connectionState.equals(WebsocketConnectionState.RECONNECTING)) {
            try {
                // hook: on pre connect
                config.onPreConnect().run();

                // Change Connection State
                setState(WebsocketConnectionState.CONNECTING);

                // init websocket
                webSocket = createWebsocket();

                // connect
                this.webSocket.connect();

                // hook: post connect
                config.onPostConnect().run();
            } catch (Exception ex) {
                final long retryDelay = config.backoffStrategy().get();
                if (retryDelay < 0) {
                    log.error("failed to connect to webSocket server {} and max retries were hit.", config.baseUrl(), ex);
                    config.backoffStrategy().reset(); // start fresh on the next manual connect() call
                    return;
                }

                log.error("connection to webSocket server {} failed: retrying ...", config.baseUrl(), ex);
                // Sleep before trying to reconnect
                try {
                    Thread.sleep(retryDelay);
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
        WebsocketConnectionState connectionState = this.connectionState.get();
        if (connectionState.equals(WebsocketConnectionState.CONNECTED) || connectionState.equals(WebsocketConnectionState.LOST)) {
            // hook: disconnecting
            config.onDisconnecting().run();

            setState(WebsocketConnectionState.DISCONNECTING);
        }

        // hook: pre disconnect
        config.onPreDisconnect().run();

        setState(WebsocketConnectionState.DISCONNECTED);

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
        setState(WebsocketConnectionState.RECONNECTING);
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
        WebsocketConnectionState connectionState = this.connectionState.get();
        if (!connectionState.equals(WebsocketConnectionState.CONNECTED) && !connectionState.equals(WebsocketConnectionState.CONNECTING)) {
            return false;
        }

        this.webSocket.sendText(message);

        return true;
    }

    /**
     * @return the socket's connection state
     */
    public WebsocketConnectionState getConnectionState() {
        return connectionState.get();
    }

    @Override
    public void close() throws Exception {
        disconnect();
    }
}
