package com.github.twitch4j.common.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * Injects ClientId Header, the User Agent and other common headers into each API Request
 */
public class TwitchClientIdInterceptor implements RequestInterceptor {

    /**
     * Client Id
     */
    private String clientId;

    /**
     * User Agent
     */
    private String userAgent;

    /**
     * Constructor
     *
     * @param clientId Client Id
     * @param userAgent User Agent
     */
    public TwitchClientIdInterceptor(String clientId, String userAgent) {
        this.clientId = clientId;
        this.userAgent = userAgent;
    }

    /**
     * Interceptor
     *
     * @param template Feign RequestTemplate
     */
    @Override
    public void apply(RequestTemplate template) {
        template.header("Client-Id", clientId);
        template.header("User-Agent", userAgent);
    }
}
