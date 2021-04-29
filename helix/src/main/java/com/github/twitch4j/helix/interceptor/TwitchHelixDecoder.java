package com.github.twitch4j.helix.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
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
            String remaining = singleFirst(response.headers().get(REMAINING_HEADER));
            if (remaining != null) {
                try {
                    interceptor.updateRemaining(token.substring(BEARER_PREFIX.length()), Integer.parseInt(remaining));
                } catch (Exception ignored) {
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
