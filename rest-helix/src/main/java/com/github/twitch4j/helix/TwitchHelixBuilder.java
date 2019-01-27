package com.github.twitch4j.helix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.twitch4j.common.builder.TwitchAPIBuilder;
import com.github.twitch4j.common.feign.interceptor.TwitchClientIdInterceptor;
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
import lombok.extern.slf4j.Slf4j;

/**
 * Twitch API - Helix
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TwitchHelixBuilder extends TwitchAPIBuilder<TwitchHelixBuilder> {

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

        // Hystrix
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", 5000);

        // Jackson ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        // - Modules
        mapper.findAndRegisterModules();

        // Feign
        TwitchHelix client = HystrixFeign.builder()
            .encoder(new JacksonEncoder(mapper))
            .decoder(new JacksonDecoder(mapper))
            .logger(new Logger.ErrorLogger())
            .errorDecoder(new TwitchHelixErrorDecoder(new JacksonDecoder()))
            .requestInterceptor(new TwitchClientIdInterceptor(this))
            .retryer(new Retryer.Default(1, 10000, 3))
            .options(new Request.Options(5000, 15000))
            .target(TwitchHelix.class, baseUrl);

        // register with serviceMediator
        getEventManager().getServiceMediator().addService("twitch4j-helix", client);

        return client;
    }
}
