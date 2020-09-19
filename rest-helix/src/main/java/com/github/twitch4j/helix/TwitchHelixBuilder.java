package com.github.twitch4j.helix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.config.Twitch4JGlobal;
import com.github.twitch4j.helix.interceptor.TwitchHelixClientIdInterceptor;
import com.netflix.config.ConfigurationManager;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.hystrix.HystrixFeign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Twitch API - Helix
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TwitchHelixBuilder {

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
     * Default Auth Token for API Requests
     */
    @With
    private OAuth2Credential defaultAuthToken = null;

    /**
     * HTTP Request Queue Size
     */
    @With
    private Integer requestQueueSize = -1;

    /**
     * BaseUrl
     */
    private String baseUrl = "https://api.twitch.tv/helix";

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
    public static TwitchHelixBuilder builder() {
        return new TwitchHelixBuilder();
    }

    /**
     * Twitch API Client (Helix)
     *
     * @return TwitchHelix
     */
    public TwitchHelix build() {
        log.debug("Helix: Initializing Module ...");

        // Hystrix
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", timeout);
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.requestCache.enabled", false);
        ConfigurationManager.getConfigInstance().setProperty("hystrix.threadpool.default.maxQueueSize", getRequestQueueSize());
        ConfigurationManager.getConfigInstance().setProperty("hystrix.threadpool.default.queueSizeRejectionThreshold", getRequestQueueSize());

        // Jackson ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        // - Modules
        mapper.findAndRegisterModules();

        // Create HttpClient with proxy
        okhttp3.OkHttpClient.Builder clientBuilder = new okhttp3.OkHttpClient.Builder();
        if (proxyConfig != null)
            proxyConfig.apply(clientBuilder);

        // Feign
        TwitchHelix client = HystrixFeign.builder()
            .client(new OkHttpClient(clientBuilder.build()))
            .encoder(new JacksonEncoder(mapper))
            .decoder(new JacksonDecoder(mapper))
            .logger(new Slf4jLogger())
            .logLevel(logLevel)
            .errorDecoder(new TwitchHelixErrorDecoder(new JacksonDecoder()))
            .requestInterceptor(new TwitchHelixClientIdInterceptor(this))
            .options(new Request.Options(timeout / 3, TimeUnit.MILLISECONDS, timeout, TimeUnit.MILLISECONDS, true))
            .retryer(new Retryer.Default(500, timeout, 2))
            .target(TwitchHelix.class, baseUrl);

        return client;
    }
}
