package com.github.twitch4j.helix.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import feign.Response;
import feign.jackson.JacksonDecoder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

import static com.github.twitch4j.helix.interceptor.TwitchHelixClientIdInterceptor.AUTH_HEADER;
import static com.github.twitch4j.helix.interceptor.TwitchHelixClientIdInterceptor.BEARER_PREFIX;

public class TwitchHelixDecoder extends JacksonDecoder {

    public static final String REMAINING_HEADER = "Ratelimit-Remaining";

    private final TwitchHelixClientIdInterceptor interceptor;

    public TwitchHelixDecoder(ObjectMapper mapper, TwitchHelixClientIdInterceptor interceptor) {
        super(mapper);
        this.interceptor = interceptor;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException {
        // track rate limit for token
        String token = singleFirst(response.request().headers().get(AUTH_HEADER));
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            // Parse remaining
            String remainingStr = singleFirst(response.headers().get(REMAINING_HEADER));
            Integer remaining;
            try {
                remaining = Integer.parseInt(remainingStr);
            } catch (NumberFormatException ignored) {
                remaining = null;
            }

            // Synchronize library buckets with twitch data
            if (remaining != null) {
                String bearer = token.substring(BEARER_PREFIX.length());
                if (response.request().httpMethod() == Request.HttpMethod.POST && response.request().requestTemplate().path().endsWith("/clips")) {
                    // Create Clip has a separate rate limit to synchronize
                    interceptor.updateRemainingCreateClip(bearer, remaining);
                } else {
                    // Normal/global helix rate limit synchronization
                    interceptor.updateRemaining(bearer, remaining);
                }
            }
        }

        // delegate to JacksonDecoder
        return super.decode(response, type);
    }

    static String singleFirst(Collection<String> collection) {
        if (collection == null || collection.size() != 1) return null;
        return collection.toArray(new String[1])[0];
    }

}
