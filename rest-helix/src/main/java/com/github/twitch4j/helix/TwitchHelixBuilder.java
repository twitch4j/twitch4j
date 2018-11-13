package com.github.twitch4j.helix;

import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.helix.interceptors.CommonHeaderInterceptor;
import com.netflix.config.ConfigurationManager;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.hystrix.HystrixFeign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;

/**
 * Twitch API - Helix
 * <p>
 * Documentation: https://dev.twitch.tv/docs/api/
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TwitchHelixBuilder {

    /**
     * Event Manager
     */
    @Wither
    private EventManager eventManager = new EventManager();

    /**
     * Client Id
     */
    @Wither
    private String clientId = "jzkbprff40iqj646a697cyrvl0zt2m6";

    /**
     * Client Secret
     */
    @Wither
    private String clientSecret = "**SECRET**";

    /**
     * User Agent
     */
    private String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";

    /**
     * BaseUrl
     */
    private String baseUrl = "https://api.twitch.tv/helix";

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
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", 2500);
        TwitchHelix client = HystrixFeign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .logger(new Logger.ErrorLogger())
            .errorDecoder(new TwitchHelixErrorDecoder(new JacksonDecoder()))
            .logLevel(Logger.Level.BASIC)
            .requestInterceptor(new CommonHeaderInterceptor(this))
            .retryer(new Retryer.Default(1, 10000, 3))
            .options(new Request.Options(5000, 15000))
            .target(TwitchHelix.class, baseUrl);

        // register with serviceMediator
        this.eventManager.getServiceMediator().addService("twitch4j-helix", client);

        return client;
    }
}
