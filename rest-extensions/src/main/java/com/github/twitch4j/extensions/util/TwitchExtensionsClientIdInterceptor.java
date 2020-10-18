package com.github.twitch4j.extensions.util;

import com.github.twitch4j.extensions.TwitchExtensionsBuilder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * Injects Client-ID and User-Agent in each API request
 */
public class TwitchExtensionsClientIdInterceptor implements RequestInterceptor {

    /**
     * Client ID Header Name
     */
    private static final String HEADER_NAME = "Client-Id";

    /**
     * Client ID
     */
    private final String defaultClientId;

    /**
     * User Agent
     */
    private final String userAgent;

    /**
     * Constructor
     *
     * @param builder Twitch Extensions Builder
     */
    public TwitchExtensionsClientIdInterceptor(TwitchExtensionsBuilder builder) {
        this.defaultClientId = builder.getClientId();
        this.userAgent = builder.getUserAgent();
    }

    /**
     * Interceptor
     *
     * @param template Feign RequestTemplate
     */
    @Override
    public void apply(RequestTemplate template) {
        // Inject Client Id header if missing
        Collection<String> headers = template.headers().get(HEADER_NAME);
        if (headers == null || headers.size() != 1 || StringUtils.isBlank(headers.iterator().next())) {
            template.removeHeader(HEADER_NAME);
            template.header(HEADER_NAME, this.defaultClientId);
        }

        // Inject Client Id in URL if missing
        String url = template.url();
        template.uri(url.startsWith("//") ? '/' + defaultClientId + '/' + url.substring(2) : url);

        // Inject User Agent
        template.header("User-Agent", userAgent);
    }

}
