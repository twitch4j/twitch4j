package com.github.twitch4j.tmi;

import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.config.Twitch4JGlobal;
import feign.Logger;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.extern.slf4j.Slf4j;

/**
 * Twitch API - Messaging Interface
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TwitchMessagingInterfaceBuilder {

    /**
     * Client Id
     */
    @With
    private String clientId = Twitch4JGlobal.clientId;

    /**
     * Client Secret
     */
    @With
    private String clientSecret = Twitch4JGlobal.clientSecret;

    /**
     * User Agent
     */
    @With
    private String userAgent = Twitch4JGlobal.userAgent;

    /**
     * HTTP Request Queue Size
     */
    @With
    private Integer requestQueueSize = -1;

    /**
     * BaseUrl
     */
    private String baseUrl = "https://tmi.twitch.tv";

    /**
     * Default Timeout
     */
    @With
    private Integer timeout = 5000;

    /**
     * you can overwrite the feign loglevel to print the full requests + responses if needed
     */
    @With
    private Logger.Level logLevel = Logger.Level.NONE;

    /**
     * Proxy Configuration
     */
    @With
    private ProxyConfig proxyConfig = null;

    /**
     * Initialize the builder
     *
     * @return Twitch Helix Builder
     */
    public static TwitchMessagingInterfaceBuilder builder() {
        return new TwitchMessagingInterfaceBuilder();
    }

    /**
     * Twitch API Client (Helix)
     *
     * @return TwitchHelix
     */
    public TwitchMessagingInterface build() {
        return TwitchMessagingInterfaceAPI.build(spec -> {
            spec.clientId(clientId);
            spec.clientSecret(clientSecret);
            spec.userAgent(userAgent);
            spec.requestQueueSize(requestQueueSize);
            spec.baseUrl(baseUrl);
            spec.timeout(timeout);
            spec.logLevel(logLevel);
            spec.proxyConfig(proxyConfig);
        });
    }
}
