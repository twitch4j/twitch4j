package com.github.twitch4j.kraken.interceptors;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import com.github.twitch4j.kraken.TwitchKrakenBuilder;

/**
 * Injects ClientId Header, the User Agent and other common headers into each API Request
 */
public class CommonHeaderInterceptor implements RequestInterceptor {

    /**
     * Reference to the Client Builder
     */
    private final TwitchKrakenBuilder twitchKrakenBuilder;

    /**
     * Constructor
     *
     * @param twitchKrakenBuilder Twitch Client Builder
     */
    public CommonHeaderInterceptor(TwitchKrakenBuilder twitchKrakenBuilder) {
        this.twitchKrakenBuilder = twitchKrakenBuilder;
    }

    /**
     * Client Auth Header Interceptor
     *
     * @param template Feign RequestTemplate
     */
    @Override
    public void apply(RequestTemplate template) {
        template.header("Client-Id", twitchKrakenBuilder.getClientId());
        template.header("User-Agent", twitchKrakenBuilder.getUserAgent());
    }
}
