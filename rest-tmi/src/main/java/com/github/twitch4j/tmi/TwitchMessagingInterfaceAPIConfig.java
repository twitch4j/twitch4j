package com.github.twitch4j.tmi;

import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.config.Twitch4JGlobal;
import feign.Logger;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;
import java.util.function.Consumer;

@Accessors(chain = true, fluent = true, prefix = "")
@Setter
@Getter
public class TwitchMessagingInterfaceAPIConfig {
    /**
     * The official base URL used by production Twitch Helix API servers.
     */
    public static final String OFFICIAL_BASE_URL = "https://tmi.twitch.tv";

    public static TwitchMessagingInterfaceAPIConfig process(Consumer<TwitchMessagingInterfaceAPIConfig> spec) {
        TwitchMessagingInterfaceAPIConfig data = new TwitchMessagingInterfaceAPIConfig();
        spec.accept(data);
        data.validate();
        return data;
    }

    /**
     * validate the config
     */
    public void validate() {
        Objects.requireNonNull(baseUrl, "baseUrl may not be null!");
    }

    /**
     * Client Id
     */
    private String clientId = Twitch4JGlobal.clientId;

    /**
     * Client Secret
     */
    private String clientSecret = Twitch4JGlobal.clientSecret;

    /**
     * User Agent
     */
    private String userAgent = Twitch4JGlobal.userAgent;

    /**
     * HTTP Request Queue Size
     */
    private Integer requestQueueSize = -1;

    /**
     * BaseUrl
     */
    private String baseUrl = OFFICIAL_BASE_URL;

    /**
     * Default Timeout
     */
    private Integer timeout = 5000;

    /**
     * you can overwrite the feign loglevel to print the full requests + responses if needed
     */
    private Logger.Level logLevel = Logger.Level.NONE;

    /**
     * Proxy Configuration
     */
    private ProxyConfig proxyConfig = null;

}
