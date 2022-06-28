package com.github.twitch4j.extensions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.config.Twitch4JGlobal;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.extensions.compat.TwitchExtensionsCompatibilityLayer;
import com.github.twitch4j.extensions.util.TwitchExtensionsClientIdInterceptor;
import com.github.twitch4j.extensions.util.TwitchExtensionsErrorDecoder;
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
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Twitch API - Extensions
 *
 * @see <a href="https://discuss.dev.twitch.tv/t/how-extensions-are-affected-by-the-legacy-twitch-api-v5-shutdown/32708">Twitch Shutdown Announcement</a>
 * @deprecated the Extensions API traditionally uses the decommissioned Kraken API. While the module now forwards calls to Helix, please migrate to using Helix directly as this module will be removed in the future.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Deprecated
public class TwitchExtensionsBuilder {

    @With
    private MeterRegistry meterRegistry = new CompositeMeterRegistry();

    /**
     * Base Url
     */
    @With
    private String baseUrl = "https://api.twitch.tv/extensions";

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
     * Default Timeout
     */
    @With
    private long timeout = 5000;

    /**
     * HTTP Request Queue Size
     */
    @With
    private int requestQueueSize = -1;

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
     * Whether the compatibility layer should be used to forward requests to the new Helix API
     */
    @With
    private boolean helixForwarding = true;

    /**
     * Twitch API Client (Extensions)
     *
     * @return TwitchExtensions
     * @see <a href="https://discuss.dev.twitch.tv/t/how-extensions-are-affected-by-the-legacy-twitch-api-v5-shutdown/32708">Twitch Shutdown Announcement</a>
     * @deprecated the Extensions API traditionally uses the decommissioned Kraken API. While the module now forwards calls to Helix, please migrate to using Helix directly as this module will be removed in the future.
     */
    @Deprecated
    public TwitchExtensions build() {
        log.debug("Extensions: Initializing Module ...");

        // Helix Compatibility Layer
        if (helixForwarding) {
            return TwitchExtensionsCompatibilityLayer.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .userAgent(userAgent)
                .timeout(timeout)
                .requestQueueSize(requestQueueSize)
                .logLevel(logLevel)
                .proxyConfig(proxyConfig)
                .build();
        }

        // Hystrix
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", timeout);
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.requestCache.enabled", false);
        ConfigurationManager.getConfigInstance().setProperty("hystrix.threadpool.default.maxQueueSize", getRequestQueueSize());
        ConfigurationManager.getConfigInstance().setProperty("hystrix.threadpool.default.queueSizeRejectionThreshold", getRequestQueueSize());

        // Warning
        if (logLevel == Logger.Level.HEADERS || logLevel == Logger.Level.FULL) {
            log.warn("Extensions: The current feign loglevel will print sensitive information including your access token, please don't share this log!");
        }

        // Jackson ObjectMapper
        ObjectMapper mapper = TypeConvert.getObjectMapper();

        // Create HttpClient with proxy
        okhttp3.OkHttpClient.Builder clientBuilder = new okhttp3.OkHttpClient.Builder();
        if (proxyConfig != null)
            proxyConfig.apply(clientBuilder);

        // Feign
        return HystrixFeign.builder()
            .client(new OkHttpClient(clientBuilder.build()))
            .encoder(new JacksonEncoder(mapper))
            .decoder(new JacksonDecoder(mapper))
            .logger(new Slf4jLogger())
            .logLevel(logLevel)
            .addCapability(new MicrometerCapability(meterRegistry))
            .errorDecoder(new TwitchExtensionsErrorDecoder(mapper, new JacksonDecoder()))
            .requestInterceptor(new TwitchExtensionsClientIdInterceptor(this))
            .options(new Request.Options(timeout / 3, TimeUnit.MILLISECONDS, timeout, TimeUnit.MILLISECONDS, true))
            .retryer(new Retryer.Default(500, timeout, 2))
            .target(TwitchExtensions.class, baseUrl);
    }

    /**
     * Initialize the builder
     *
     * @return Twitch Extensions Builder
     * @see <a href="https://discuss.dev.twitch.tv/t/how-extensions-are-affected-by-the-legacy-twitch-api-v5-shutdown/32708">Twitch Shutdown Announcement</a>
     * @deprecated the Extensions API traditionally uses the decommissioned Kraken API. While the module now forwards calls to Helix, please migrate to using Helix directly as this module will be removed in the future.
     */
    @Deprecated
    public static TwitchExtensionsBuilder builder() {
        return new TwitchExtensionsBuilder();
    }
}
