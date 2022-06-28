package com.github.twitch4j.kraken;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.config.Twitch4JGlobal;
import com.github.twitch4j.common.feign.interceptor.JsonContentTypeHeaderInterceptor;
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
 * @deprecated Kraken is deprecated and has been shut down on <b>Febuary 28, 2022</b>.
 *             More details about the deprecation are available <a href="https://blog.twitch.tv/en/2021/07/15/legacy-twitch-api-v5-shutdown-details-and-timeline">here</a>.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Deprecated
public class TwitchKrakenBuilder {

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
    @With
    private String baseUrl = "https://api.twitch.tv/kraken";

    /**
     * Default Timeout
     */
    @With
    private Integer timeout = 5000;

    @With
    private Integer uploadTimeout = 4 * 60 * 1000;

    /**
     * you can overwrite the feign loglevel to print the full requests + responses if needed
     */
    @With
    private Logger.Level logLevel = Logger.Level.NONE;

    /**
     * ProxyConfiguration
     */
    @With
    private ProxyConfig proxyConfig = null;

    /**
     * Initialize the builder
     *
     * @return Twitch Kraken Builder
     */
    public static TwitchKrakenBuilder builder() {
        return new TwitchKrakenBuilder();
    }

    /**
     * Twitch API Client (Kraken)
     *
     * @return TwitchKraken
     */
    @Deprecated
    public TwitchKraken build() {
        log.warn("Kraken is deprecated and has been shut down on Febuary 28, 2022.");
        log.warn("More details about the decommission are available here: https://blog.twitch.tv/en/2021/07/15/legacy-twitch-api-v5-shutdown-details-and-timeline");
        log.debug("Kraken: Initializing Module ...");

        // Hystrix
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", timeout);
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.requestCache.enabled", false);
        ConfigurationManager.getConfigInstance().setProperty("hystrix.threadpool.default.maxQueueSize", getRequestQueueSize());
        ConfigurationManager.getConfigInstance().setProperty("hystrix.threadpool.default.queueSizeRejectionThreshold", getRequestQueueSize());

        // Hystrix: Timeout modification for file uploads
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.TwitchKraken#uploadVideoPart(URI,String,String,int,byte[]).execution.isolation.thread.timeoutInMilliseconds", uploadTimeout);

        // Hystrix: Timeout modification for emote endpoints with large amount of data
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.TwitchKraken#getChatEmoticonsBySet(Collection).execution.isolation.thread.timeoutInMilliseconds", timeout * 2);
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.TwitchKraken#getChatEmoticons().execution.isolation.thread.timeoutInMilliseconds", timeout * 4);
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.TwitchKraken#getAllChatEmoticons().execution.isolation.thread.timeoutInMilliseconds", timeout * 8);

        // Warning
        if (logLevel == Logger.Level.HEADERS || logLevel == Logger.Level.FULL) {
            log.warn("Kraken: The current feign loglevel will print sensitive information including your access token, please don't share this log!");
        }

        // Jackson ObjectMapper
        ObjectMapper mapper = TypeConvert.getObjectMapper();

        // Create HttpClient with proxy
        okhttp3.OkHttpClient.Builder clientBuilder = new okhttp3.OkHttpClient.Builder();
        if (proxyConfig != null)
            proxyConfig.apply(clientBuilder);

        // Build
        return HystrixFeign.builder()
            .client(new OkHttpClient(clientBuilder.build()))
            .encoder(new JacksonEncoder(mapper))
            .decoder(new JacksonDecoder(mapper))
            .logger(new Slf4jLogger())
            .logLevel(logLevel)
            .addCapability(new MicrometerCapability(meterRegistry))
            .errorDecoder(new TwitchKrakenErrorDecoder(new JacksonDecoder()))
            .requestInterceptor(new TwitchClientIdInterceptor(this.clientId, this.userAgent))
            .requestInterceptor(t -> t.header("Accept", "application/vnd.twitchtv.v5+json"))
            .requestInterceptor(new JsonContentTypeHeaderInterceptor())
            .options(new Request.Options(timeout / 3, TimeUnit.MILLISECONDS, timeout, TimeUnit.MILLISECONDS, true))
            .retryer(new Retryer.Default(500, timeout, 2))
            .target(TwitchKraken.class, baseUrl);
    }
}
