package com.github.twitch4j.helix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.twitch4j.common.feign.capability.TwitchFeignCapability;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.helix.api.TwitchHelixApi;
import com.github.twitch4j.helix.domain.CustomReward;
import com.github.twitch4j.helix.interceptor.CustomRewardEncodeMixIn;
import com.github.twitch4j.helix.interceptor.TwitchHelixClientIdInterceptor;
import com.github.twitch4j.helix.interceptor.TwitchHelixDecoder;
import com.github.twitch4j.helix.interceptor.TwitchHelixHttpClient;
import com.github.twitch4j.helix.interceptor.TwitchHelixRateLimitTracker;
import com.github.twitch4j.helix.interceptor.TwitchHelixTokenManager;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Credentials;
import org.jetbrains.annotations.ApiStatus;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * A factory to build instances of {@link TwitchHelix}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@ApiStatus.Experimental
@Slf4j
public class TwitchHelixFactory {

    /**
     * Creates a new {@link TwitchHelix} instance.
     * <p>
     * This class allows the creation of validated {@link TwitchHelix} instances using a consumer-based specification pattern.
     * This method also allows the creation alternative API interface variants, which provide alternative methods for interaction methods.
     *
     * @param spec The specification to construct the {@link TwitchHelix} instance.
     * @return The created instance of {@link TwitchHelix}.
     * @param <T> The type of the API interface
     */
    public static <T> T create(Consumer<TwitchHelixFactorySpec<T>> spec) {
        TwitchHelixFactorySpec<T> config = new TwitchHelixFactorySpec<>(spec);

        if (config.api().isInterface()) {
            return buildClient(spec);
        } else {
            try {
                TwitchHelixApi api = buildClient(s -> {
                    s.api(TwitchHelixApi.class);
                    s.applySpec(config);
                });
                return config.api().getConstructor(TwitchHelixApi.class).newInstance(api);
            } catch (Exception e) {
                throw new IllegalArgumentException("api must have a constructor with one parameter of type PagerDutyEventsV2Api");
            }
        }
    }

    private static <T> T buildClient(Consumer<TwitchHelixFactorySpec<T>> spec) {
        TwitchHelixFactorySpec<T> config = new TwitchHelixFactorySpec<>(spec);

        // Warning
        if (config.logLevel() == Logger.Level.HEADERS || config.logLevel() == Logger.Level.FULL) {
            log.warn("Helix: The current feign loglevel will print sensitive information including your access token, please don't share this log!");
        }

        // http client
        okhttp3.OkHttpClient.Builder clientBuilder = new okhttp3.OkHttpClient.Builder();
        if (config.proxy() != null && config.proxy().type() != Proxy.Type.DIRECT) {
            clientBuilder.proxy(new Proxy(config.proxy().type(), new InetSocketAddress(config.proxy().host(), config.proxy().port())));
            if (config.proxy().username() != null || config.proxy().password() != null) {
                clientBuilder.proxyAuthenticator((route, response) -> response.request().newBuilder()
                    .header("Proxy-Authorization", Credentials.basic(config.proxy().username(), new String(config.proxy().password())))
                    .build());
            }
        }

        // objectMapper
        ObjectMapper mapper = TypeConvert.getObjectMapper();
        ObjectMapper serializer = mapper.copy().addMixIn(CustomReward.class, CustomRewardEncodeMixIn.class);

        // feign
        TwitchHelixTokenManager tokenManager = new TwitchHelixTokenManager(config.clientId(), config.clientSecret(), config.defaultAuthToken());
        TwitchHelixRateLimitTracker rateLimitTracker = new TwitchHelixRateLimitTracker(config.apiRateLimit(), tokenManager);
        return Feign.builder()
            .client(new TwitchHelixHttpClient(new OkHttpClient(clientBuilder.build()), config.scheduledThreadPoolExecutor(), tokenManager, rateLimitTracker, config.timeout()))
            .encoder(new JacksonEncoder(serializer))
            .decoder(new TwitchHelixDecoder(mapper, rateLimitTracker))
            .logger(new Slf4jLogger())
            .logLevel(config.logLevel())
            .errorDecoder(new TwitchHelixErrorDecoder(new JacksonDecoder(), rateLimitTracker))
            .requestInterceptor(new TwitchHelixClientIdInterceptor(config.userAgent(), tokenManager))
            .options(new Request.Options(config.timeout() / 3, TimeUnit.MILLISECONDS, config.timeout(), TimeUnit.MILLISECONDS, true))
            .retryer(new Retryer.Default(500, config.timeout(), 2))
            .addCapability(new TwitchFeignCapability(config.backendName(), config.extensions()))
            .target(config.api(), config.baseUrl());
    }
}
