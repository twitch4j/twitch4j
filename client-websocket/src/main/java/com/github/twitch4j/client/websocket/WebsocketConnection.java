package com.github.twitch4j.client.websocket;

import com.github.twitch4j.client.websocket.domain.WebsocketConnectionState;
import com.github.twitch4j.common.util.ExponentialBackoffStrategy;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketCloseCode;
import com.neovisionaries.ws.client.WebSocketError;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
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

    protected final AtomicBoolean closed = new AtomicBoolean(false);

    protected final CountDownLatch closeLatch = new CountDownLatch(1);

    /**
     * TwitchWebsocketConnection
     *
     * @param configSpec the websocket connection configuration
     */
    public WebsocketConnection(Consumer<WebsocketConnectionConfig> configSpec) {
        config = WebsocketConnectionConfig.process(configSpec);

        // webSocketFactory and proxy configuration
        this.webSocketFactory = new WebSocketFactory()
            .setConnectionTimeout(config.connectionTimeout())
            .setSocketTimeout(config.socketTimeout());

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
                    closeSocket(); // avoid possible resource leak
                    setState(WebsocketConnectionState.LOST);
                    log.info("Connection to WebSocket [{}] lost! Retrying soon ...", config.baseUrl());

                    // connection lost - reconnecting
                    if (backoffClearer != null) backoffClearer.cancel(false);
                    long reconnectDelay = config.backoffStrategy().get();
                    if (reconnectDelay < 0) {
                        log.debug("Maximum retry count for websocket reconnection attempts was hit.");
                        config.backoffStrategy().reset(); // start fresh on the next manual connect() call
                    } else {
                        config.taskExecutor().schedule(() -> {
                            WebsocketConnectionState state = connectionState.get();
                            if (state != WebsocketConnectionState.CONNECTING && state != WebsocketConnectionState.CONNECTED)
                                reconnect();
                        }, reconnectDelay, TimeUnit.MILLISECONDS);
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
        ws.setMissingCloseFrameAllowed(true);
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
        if (connectionState == WebsocketConnectionState.DISCONNECTED || connectionState == WebsocketConnectionState.RECONNECTING || connectionState == WebsocketConnectionState.LOST) {
            if (closed.get())
                throw new IllegalStateException("WebsocketConnection was already closed!");

            try {
                // avoid any resource leaks
                this.closeSocket();

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

        if (connectionState == WebsocketConnectionState.DISCONNECTED) {
            // have already disconnected
            return;
        }

        if (connectionState == WebsocketConnectionState.CONNECTED || connectionState == WebsocketConnectionState.LOST) {
            // hook: disconnecting
            config.onDisconnecting().run();

            setState(WebsocketConnectionState.DISCONNECTING);
        }

        // hook: pre disconnect
        config.onPreDisconnect().run();

        // CleanUp
        this.closeSocket();

        // update state
        setState(WebsocketConnectionState.DISCONNECTED);

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
        if (connectionState != WebsocketConnectionState.CONNECTED && connectionState != WebsocketConnectionState.CONNECTING) {
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
        if (closed.getAndSet(true))
            return; // resource close was already requested

        if (backoffClearer != null)
            backoffClearer.cancel(false);

        try {
            disconnect();
        } catch (Exception e) {
            log.warn("Exception thrown from websocket disconnect attempt", e);
            this.closeSocket(); // really make sure the resource was released
        } finally {
            // await the close of the underlying socket
            try {
                boolean completed = closeLatch.await(Math.max(1000, config.closeDelay()) * 2L, TimeUnit.MILLISECONDS);
                if (completed) {
                    log.trace("Underlying websocket complete close was successful");
                } else {
                    log.warn("Underlying websocket did not close within the expected delay");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Synchronized
    private void closeSocket() {
        // Clean up the socket
        final WebSocket socket = this.webSocket;
        if (socket != null) {
            socket.clearListeners();
            if (closed.get()) {
                socket.addListener(new WebSocketAdapter() {
                    @Override
                    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
                        socket.clearListeners();
                        closeLatch.countDown();
                    }

                    @Override
                    public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) {
                        if (cause != null && cause.getError() == WebSocketError.FLUSH_ERROR) {
                            socket.clearListeners();
                            closeLatch.countDown();
                        }
                    }
                });
            }
            socket.disconnect(WebSocketCloseCode.NORMAL, null, config.closeDelay());

            this.webSocket = null;
        }

        // Reset latency tracker
        this.latency = -1L;
        lastPing.lazySet(0L);
    }

}
