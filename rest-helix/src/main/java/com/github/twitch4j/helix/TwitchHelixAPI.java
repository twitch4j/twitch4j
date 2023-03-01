package com.github.twitch4j.helix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.helix.domain.CustomReward;
import com.github.twitch4j.helix.interceptor.CustomRewardEncodeMixIn;
import com.github.twitch4j.helix.interceptor.TwitchHelixClientIdInterceptor;
import com.github.twitch4j.helix.interceptor.TwitchHelixDecoder;
import com.github.twitch4j.helix.interceptor.TwitchHelixHttpClient;
import com.github.twitch4j.helix.interceptor.TwitchHelixRateLimitTracker;
import com.github.twitch4j.helix.interceptor.TwitchHelixTokenManager;
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
public class TwitchHelixAPI {

    /**
     * Constructs a Twitch API client instance.
     *
     * @param configSpec the Helix API configuration
     * @return {@link TwitchHelix}
     */
    public TwitchHelix build(Consumer<TwitchHelixAPIConfig> configSpec) {
        TwitchHelixAPIConfig config = TwitchHelixAPIConfig.process(configSpec);

        // Hystrix
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", config.timeout());
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.requestCache.enabled", false);
        ConfigurationManager.getConfigInstance().setProperty("hystrix.threadpool.default.maxQueueSize", config.requestQueueSize());
        ConfigurationManager.getConfigInstance().setProperty("hystrix.threadpool.default.queueSizeRejectionThreshold", config.requestQueueSize());

        // Hystrix: Ban/Unban API already has special 429 logic such that circuit breaking is not needed (and just trips on trivial errors like 'user is already banned')
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.TwitchHelix#banUser(String,String,String,BanUserInput).circuitBreaker.enabled", false);
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.TwitchHelix#unbanUser(String,String,String,String).circuitBreaker.enabled", false);

        // Warning
        if (config.logLevel() == Logger.Level.HEADERS || config.logLevel() == Logger.Level.FULL) {
            log.warn("Helix: The current feign loglevel will print sensitive information including your access token, please don't share this log!");
        }

        // Jackson ObjectMapper
        ObjectMapper mapper = TypeConvert.getObjectMapper();
        ObjectMapper serializer = mapper.copy().addMixIn(CustomReward.class, CustomRewardEncodeMixIn.class);

        // Create HttpClient with proxy
        okhttp3.OkHttpClient.Builder clientBuilder = new okhttp3.OkHttpClient.Builder();
        if (config.proxyConfig() != null)
            config.proxyConfig().apply(clientBuilder);

        // Feign
        TwitchHelixTokenManager tokenManager = new TwitchHelixTokenManager(config.clientId(), config.clientSecret(), config.defaultAuthToken());
        TwitchHelixRateLimitTracker rateLimitTracker = new TwitchHelixRateLimitTracker(config.apiRateLimit(), tokenManager);
        return HystrixFeign.builder()
                .client(new TwitchHelixHttpClient(new OkHttpClient(clientBuilder.build()), config.scheduledThreadPoolExecutor(), tokenManager, rateLimitTracker, config.timeout()))
                .encoder(new JacksonEncoder(serializer))
                .decoder(new TwitchHelixDecoder(mapper, rateLimitTracker))
                .logger(new Slf4jLogger())
                .logLevel(config.logLevel())
                .errorDecoder(new TwitchHelixErrorDecoder(new JacksonDecoder(), rateLimitTracker))
                .requestInterceptor(new TwitchHelixClientIdInterceptor(config.userAgent(), tokenManager))
                .options(new Request.Options(config.timeout() / 3, TimeUnit.MILLISECONDS, config.timeout(), TimeUnit.MILLISECONDS, true))
                .retryer(new Retryer.Default(500, config.timeout(), 2))
                .target(TwitchHelix.class, config.baseUrl());
    }
}
