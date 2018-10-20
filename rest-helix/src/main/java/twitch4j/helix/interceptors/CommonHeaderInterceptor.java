package twitch4j.helix.interceptors;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import twitch4j.helix.TwitchHelixBuilder;

/**
 * Injects ClientId Header, the User Agent and other common headers into each API Request
 */
public class CommonHeaderInterceptor implements RequestInterceptor {

    /**
     * Reference to the Client Builder
     */
    private final TwitchHelixBuilder twitchHelixBuilder;

    /**
     * Constructor
     *
     * @param twitchHelixBuilder Twitch Client Builder
     */
    public CommonHeaderInterceptor(TwitchHelixBuilder twitchHelixBuilder) {
        this.twitchHelixBuilder = twitchHelixBuilder;
    }

    /**
     * Client Auth Header Interceptor
     *
     * @param template Feign RequestTemplate
     */
    @Override
    public void apply(RequestTemplate template) {
        template.header("Client-Id", twitchHelixBuilder.getClientId());
        template.header("User-Agent", twitchHelixBuilder.getUserAgent());
    }
}