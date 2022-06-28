package com.github.twitch4j.tmi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.config.Twitch4JGlobal;
import com.github.twitch4j.common.feign.interceptor.TwitchClientIdInterceptor;
import com.github.twitch4j.common.util.TypeConvert;
import com.netflix.config.ConfigurationManager;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.hystrix.HystrixFeign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.micrometer.MicrometerCapability;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Twitch API - Messaging Interface
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TwitchMessagingInterfaceBuilder {

    @With
    private MeterRegistry meterRegistry = new CompositeMeterRegistry();

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
        log.debug("TMI: Initializing Module ...");

        // Hystrix
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", timeout);
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.requestCache.enabled", false);
        ConfigurationManager.getConfigInstance().setProperty("hystrix.threadpool.default.maxQueueSize", getRequestQueueSize());
        ConfigurationManager.getConfigInstance().setProperty("hystrix.threadpool.default.queueSizeRejectionThreshold", getRequestQueueSize());

        // Warning
        if (logLevel == Logger.Level.HEADERS || logLevel == Logger.Level.FULL) {
            log.warn("TMI: The current feign loglevel will print sensitive information including your access token, please don't share this log!");
        }

        // Create HttpClient with proxy
        okhttp3.OkHttpClient.Builder clientBuilder = new okhttp3.OkHttpClient.Builder();
        if (proxyConfig != null)
            proxyConfig.apply(clientBuilder);

        ObjectMapper mapper = TypeConvert.getObjectMapper();

        // Build
        TwitchMessagingInterface client = HystrixFeign.builder()
            .client(new OkHttpClient(clientBuilder.build()))
            .encoder(new JacksonEncoder(mapper))
            .decoder(new JacksonDecoder(mapper))
            .logger(new Slf4jLogger())
            .logLevel(logLevel)
            .addCapability(new MicrometerCapability(meterRegistry))
            .errorDecoder(new TwitchMessagingInterfaceErrorDecoder(new JacksonDecoder()))
            .requestInterceptor(new TwitchClientIdInterceptor(this.clientId, this.userAgent))
            .retryer(new Retryer.Default(1, 10000, 3))
            .options(new Request.Options(5000, TimeUnit.MILLISECONDS, 15000, TimeUnit.MILLISECONDS, true))
            .target(TwitchMessagingInterface.class, baseUrl);

        return client;
    }
}
