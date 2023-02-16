package com.github.twitch4j.helix.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import feign.Response;
import feign.jackson.JacksonDecoder;

import java.io.IOException;
import java.lang.reflect.Type;

import static com.github.twitch4j.helix.interceptor.TwitchHelixClientIdInterceptor.AUTH_HEADER;
import static com.github.twitch4j.helix.interceptor.TwitchHelixClientIdInterceptor.BEARER_PREFIX;
import static com.github.twitch4j.helix.interceptor.TwitchHelixClientIdInterceptor.CLIENT_HEADER;
import static com.github.twitch4j.helix.interceptor.TwitchHelixHttpClient.getFirst;
import static com.github.twitch4j.helix.interceptor.TwitchHelixHttpClient.getFirstHeader;
import static com.github.twitch4j.helix.interceptor.TwitchHelixHttpClient.getFirstParam;

public class TwitchHelixDecoder extends JacksonDecoder {

    public static final String REMAINING_HEADER = "Ratelimit-Remaining";

    private final TwitchHelixRateLimitTracker rateLimitTracker;

    public TwitchHelixDecoder(ObjectMapper mapper, TwitchHelixRateLimitTracker rateLimitTracker) {
        super(mapper);
        this.rateLimitTracker = rateLimitTracker;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException {
        // track rate limit for token
        Request request = response.request();
        String token = getFirstHeader(AUTH_HEADER, request);
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            // Parse remaining
            String remainingStr = getFirst(REMAINING_HEADER, response.headers());
            Integer remaining;
            try {
                remaining = Integer.parseInt(remainingStr);
            } catch (NumberFormatException ignored) {
                remaining = null;
            }

            // Synchronize library buckets with twitch data
            if (remaining != null) {
                String bearer = token.substring(BEARER_PREFIX.length());
                if (request.httpMethod() == Request.HttpMethod.POST && request.requestTemplate().path().endsWith("/clips")) {
                    // Create Clip has a separate rate limit to synchronize
                    rateLimitTracker.updateRemainingCreateClip(bearer, remaining);
                } else if (request.httpMethod() == Request.HttpMethod.POST && request.requestTemplate().path().endsWith("/extensions/chat")) {
                    // Send Extension Chat Message rate limit
                    String clientId = getFirstHeader(CLIENT_HEADER, request);
                    String channelId = getFirstParam("broadcaster_id", request);
                    rateLimitTracker.updateRemainingExtensionChat(clientId, channelId, remaining);
                } else if (request.httpMethod() == Request.HttpMethod.POST && request.requestTemplate().path().endsWith("/extensions/pubsub")) {
                    // Send Extension PubSub Message rate limit
                    String clientId = getFirstHeader(CLIENT_HEADER, request);
                    String target = getFirstHeader("Twitch4J-Target", request);
                    rateLimitTracker.updateRemainingExtensionPubSub(clientId, target, remaining);
                } else {
                    // Normal/global helix rate limit synchronization
                    rateLimitTracker.updateRemaining(bearer, remaining);
                }
            }
        }

        // delegate to JacksonDecoder
        return super.decode(response, type);
    }

}
