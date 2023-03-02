package com.github.twitch4j.tmi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.twitch4j.common.feign.interceptor.TwitchClientIdInterceptor;
import com.github.twitch4j.common.util.TypeConvert;
import com.netflix.config.ConfigurationManager;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.hystrix.HystrixFeign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Slf4j
@UtilityClass
public class TwitchMessagingInterfaceAPI {

    /**
     * Constructs a TwitchMessagingInterface client instance.
     *
     * @param configSpec the TwitchMessagingInterface API configuration
     * @return {@link TwitchMessagingInterface}
     */
    public TwitchMessagingInterface build(Consumer<TwitchMessagingInterfaceAPIConfig> configSpec) {
        TwitchMessagingInterfaceAPIConfig config = TwitchMessagingInterfaceAPIConfig.process(configSpec);

        // Hystrix
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", config.timeout());
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.requestCache.enabled", false);
        ConfigurationManager.getConfigInstance().setProperty("hystrix.threadpool.default.maxQueueSize", config.requestQueueSize());
        ConfigurationManager.getConfigInstance().setProperty("hystrix.threadpool.default.queueSizeRejectionThreshold", config.requestQueueSize());

        // Warning
        if (config.logLevel() == Logger.Level.HEADERS || config.logLevel() == Logger.Level.FULL) {
            log.warn("TMI: The current feign loglevel will print sensitive information including your access token, please don't share this log!");
        }

        // Create HttpClient with proxy
        okhttp3.OkHttpClient.Builder clientBuilder = new okhttp3.OkHttpClient.Builder();
        if (config.proxyConfig() != null)
            config.proxyConfig().apply(clientBuilder);

        // Feign
        ObjectMapper mapper = TypeConvert.getObjectMapper();
        return HystrixFeign.builder()
            .client(new OkHttpClient(clientBuilder.build()))
            .encoder(new JacksonEncoder(mapper))
            .decoder(new JacksonDecoder(mapper))
            .logger(new Slf4jLogger())
            .logLevel(config.logLevel())
            .errorDecoder(new TwitchMessagingInterfaceErrorDecoder(new JacksonDecoder()))
            .requestInterceptor(new TwitchClientIdInterceptor(config.clientId(), config.userAgent()))
            .retryer(new Retryer.Default(1, 10000, 3))
            .options(new Request.Options(5000, TimeUnit.MILLISECONDS, 15000, TimeUnit.MILLISECONDS, true))
            .target(TwitchMessagingInterface.class, config.baseUrl());
    }
}
