package com.github.twitch4j.client.websocket;

import com.github.twitch4j.client.websocket.domain.WebsocketConnectionState;
import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.util.ExponentialBackoffStrategy;
import com.github.twitch4j.util.IBackoffStrategy;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Accessors(chain = true, fluent = true, prefix = "")
@Setter
@Getter
public class WebsocketConnectionConfig {

    public static WebsocketConnectionConfig process(Consumer<WebsocketConnectionConfig> spec) {
        WebsocketConnectionConfig data = new WebsocketConnectionConfig();
        spec.accept(data);
        data.validate();
        return data;
    }

    /**
     * validate the config
     */
    public void validate() {
        Objects.requireNonNull(baseUrl, "baseUrl may not be null!");
        if (wsPingPeriod < 0) {
            throw new RuntimeException("wsPingPeriod must be 0 or greater, set to 0 to disable!");
        }
        Objects.requireNonNull(taskExecutor, "taskExecutor may not be null!");
        Objects.requireNonNull(backoffStrategy, "backoffStrategy may not be null!");
        Objects.requireNonNull(onStateChanged, "onStateChanged may not be null!");
        Objects.requireNonNull(onPreConnect, "onPreConnect may not be null!");
        Objects.requireNonNull(onPostConnect, "onPostConnect may not be null!");
        Objects.requireNonNull(onConnected, "onConnected may not be null!");
        Objects.requireNonNull(onTextMessage, "onTextMessage may not be null!");
        Objects.requireNonNull(onTextMessage, "onTextMessage may not be null!");
        Objects.requireNonNull(onDisconnecting, "onDisconnecting may not be null!");
        Objects.requireNonNull(onPreDisconnect, "onPreDisconnect may not be null!");
        Objects.requireNonNull(onPostDisconnect, "onPostDisconnect may not be null!");
    }

    /**
     * The websocket url for the chat client to connect to.
     */
    private String baseUrl;

    /**
     * WebSocket RFC Ping Period in ms (0 = disabled)
     */
    private int wsPingPeriod = 0;

    /**
     * WebSocket Headers
     */
    private Map<String, String> headers = null;

    /**
     * Task Executor
     */
    private ScheduledExecutorService taskExecutor = new ScheduledThreadPoolExecutor(2);

    /**
     * Helper class to compute delays between connection retries
     */
    private IBackoffStrategy backoffStrategy = ExponentialBackoffStrategy.builder()
        .immediateFirst(false)
        .baseMillis(Duration.ofSeconds(1).toMillis())
        .jitter(true)
        .multiplier(2.0)
        .maximumBackoff(Duration.ofMinutes(5).toMillis())
        .build();

    /**
     * called when the websocket's state changes
     */
    private BiConsumer<WebsocketConnectionState, WebsocketConnectionState> onStateChanged = (oldState, newState) -> {};

    /**
     * called before connecting
     */
    private Runnable onPreConnect = () -> {};

    /**
     * called after connecting
     */
    private Runnable onPostConnect = () -> {};

    /**
     * called after the connection to the websocket server has been established successfully
     */
    private Runnable onConnected = () -> {};

    /**
     * called when receiving a text message on from the websocket server
     */
    private Consumer<String> onTextMessage = (text) -> {};

    /**
     * called when connection state is changing from CONNECTED to DISCONNECTING
     */
    private Runnable onDisconnecting = () -> {};

    /**
     * called before the disconnect
     * <p>
     * this occurs after onDisconnecting and before the connection is disposed
     */
    private Runnable onPreDisconnect = () -> {};

    /**
     * called after the disconnect
     */
    private Runnable onPostDisconnect = () -> {};

    /**
     * proxy config
     */
    private ProxyConfig proxyConfig;
}
