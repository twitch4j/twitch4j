package com.github.twitch4j.client.websocket;

import com.github.twitch4j.common.util.ExponentialBackoffStrategy;
import com.github.twitch4j.common.util.ThreadUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
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
        Objects.requireNonNull(baseUrl, "baseUrl is a required parameter!");
        Objects.requireNonNull(wsPingPeriod, "wsPingPeriod is a required parameter, set to 0 to disable!");
        Objects.requireNonNull(taskExecutor, "taskExecutor is a required parameter!");
        Objects.requireNonNull(backoffStrategy, "backoffStrategy is a required parameter!");
    }

    /**
     * The websocket url for the chat client to connect to.
     */
    private String baseUrl;

    /**
     * WebSocket RFC Ping Period in ms (0 = disabled)
     */
    private int wsPingPeriod = 0;

    private ScheduledExecutorService taskExecutor = ThreadUtils.getDefaultScheduledThreadPoolExecutor("twitch4j-" + RandomStringUtils.random(4, true, true), 2);

    /**
     * Helper class to compute delays between connection retries
     */
    private ExponentialBackoffStrategy backoffStrategy = ExponentialBackoffStrategy.builder()
        .immediateFirst(true)
        .baseMillis(Duration.ofSeconds(1).toMillis())
        .jitter(true)
        .multiplier(2.0)
        .maximumBackoff(Duration.ofMinutes(5).toMillis())
        .build();

    /**
     * called before connecting
     */
    private Runnable onPreConnect;

    /**
     * called after connecting
     */
    private Runnable onPostConnect;

    /**
     * called after the connection to the websocket server has been established successfully
     */
    private Runnable onConnected;

    /**
     * called when receiving a text message on from the websocket server
     */
    private Consumer<String> onTextMessage;

    /**
     * called when connection state is changing from CONNECTED to DISCONNECTING
     */
    private Runnable onDisconnecting;

    /**
     * called before the disconnect
     * <p>
     * this occurs after onDisconnecting and before the connection is disposed
     */
    private Runnable onPreDisconnect;

    /**
     * called after the disconnect
     */
    private Runnable onPostDisconnect;

    private String proxyHost;
    private Integer proxyPort;
    private String proxyUsername;
    private String proxyPassword;
}
