package com.github.twitch4j.client.websocket;

import com.github.twitch4j.client.websocket.domain.WebsocketConnectionState;
import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.util.ExponentialBackoffStrategy;
import com.github.twitch4j.util.IBackoffStrategy;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        Objects.requireNonNull(connectionName, "connectionName may not be null!");
        Objects.requireNonNull(connectionId, "connectionId may not be null!");
        Objects.requireNonNull(meterRegistry, "meterRegistry may not be null!");
        Objects.requireNonNull(baseUrl, "baseUrl may not be null!");
        if (wsPingPeriod < 0) {
            throw new RuntimeException("wsPingPeriod must be 0 or greater, set to 0 to disable!");
        }
        if (connectionTimeout < 0) {
            throw new RuntimeException("connectionTimeout must be 0 or greater, set to 0 to disable!");
        }
        if (socketTimeout < 0) {
            throw new RuntimeException("socketTimeout must be 0 or greater, set to 0 to disable!");
        }
        if (closeDelay < 0) {
            throw new RuntimeException("closeDelay must be 0 or greater!");
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
        Objects.requireNonNull(onCloseFrame, "onCloseFrame may not be null!");
    }

    /**
     * connection type
     */
    @NotNull
    private String connectionName;

    /**
     * connection index
     */
    @NotNull
    private String connectionId;

    /**
     * micrometer registry
     */
    @NotNull
    private MeterRegistry meterRegistry = new CompositeMeterRegistry(Clock.SYSTEM);

    /**
     * The websocket url for the chat client to connect to.
     */
    @NotNull
    private String baseUrl;

    /**
     * WebSocket RFC Ping Period in ms (0 = disabled)
     */
    @NotNull
    private int wsPingPeriod = 0;

    /**
     * Websocket timeout milliseconds for establishing a connection (0 = disabled).
     */
    private int connectionTimeout = 60_000;

    /**
     * Websocket timeout milliseconds for read and write operations (0 = disabled).
     */
    private int socketTimeout = 30_000;

    /**
     * The maximum number of milliseconds to wait after sending a close frame
     * to receive confirmation from the server, before fully closing the socket.
     * <p>
     * This can be set as low as 0 for applications that require prompt socket closes upon disconnect calls.
     */
    private int closeDelay = 1_000;

    /**
     * WebSocket Headers
     */
    private Map<String, String> headers = null;

    /**
     * Task Executor
     */
    @NotNull
    private ScheduledExecutorService taskExecutor = new ScheduledThreadPoolExecutor(2);

    /**
     * Helper class to compute delays between connection retries
     */
    @NotNull
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
    @NotNull
    private BiConsumer<WebsocketConnectionState, WebsocketConnectionState> onStateChanged = (oldState, newState) -> {};

    /**
     * called before connecting
     */
    @NotNull
    private Runnable onPreConnect = () -> {};

    /**
     * called after connecting
     */
    @NotNull
    private Runnable onPostConnect = () -> {};

    /**
     * called after the connection to the websocket server has been established successfully
     */
    @NotNull
    private Runnable onConnected = () -> {};

    /**
     * called when receiving a text message on from the websocket server
     */
    @NotNull
    private Consumer<String> onTextMessage = (text) -> {};

    /**
     * called when connection state is changing from CONNECTED to DISCONNECTING
     */
    @NotNull
    private Runnable onDisconnecting = () -> {};

    /**
     * called before the disconnect
     * <p>
     * this occurs after onDisconnecting and before the connection is disposed
     */
    @NotNull
    private Runnable onPreDisconnect = () -> {};

    /**
     * called after the disconnect
     */
    @NotNull
    private Runnable onPostDisconnect = () -> {};

    /**
     * called after receiving a close frame from the server
     */
    private BiConsumer<@NotNull Integer, @Nullable String> onCloseFrame = (code, reason) -> {};

    /**
     * proxy config
     */
    private ProxyConfig proxyConfig;

}
