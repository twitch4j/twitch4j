package com.github.twitch4j.helix.interceptor;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import feign.Client;
import feign.Request;
import feign.Response;
import feign.okhttp.OkHttpClient;
import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.github.twitch4j.helix.interceptor.TwitchHelixClientIdInterceptor.AUTH_HEADER;
import static com.github.twitch4j.helix.interceptor.TwitchHelixClientIdInterceptor.BEARER_PREFIX;
import static com.github.twitch4j.helix.interceptor.TwitchHelixDecoder.singleFirst;

@Slf4j
public class TwitchHelixHttpClient implements Client {

    private final Client client;
    private final ScheduledExecutorService executor;
    private final TwitchHelixClientIdInterceptor interceptor;
    private final long timeout;

    public TwitchHelixHttpClient(OkHttpClient client, ScheduledThreadPoolExecutor executor, TwitchHelixClientIdInterceptor interceptor, Integer timeout) {
        this.client = client;
        this.executor = executor;
        this.interceptor = interceptor;
        this.timeout = timeout == null ? 60 * 1000 : timeout.longValue();
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        // Check whether this request should be delayed to conform to rate limits
        String token = singleFirst(request.headers().get(AUTH_HEADER));
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            OAuth2Credential credential = interceptor.getAccessTokenCache().getIfPresent(token.substring(BEARER_PREFIX.length()));
            if (credential != null) {
                Bucket bucket = interceptor.getOrInitializeBucket(interceptor.getKey(credential));
                if (bucket.tryConsume(1)) {
                    // no delay needed
                    return client.execute(request, options);
                } else {
                    try {
                        // effectively blocking, unfortunately
                        return bucket.asScheduler().consume(1, executor)
                            .thenApplyAsync(v -> {
                                try {
                                    return client.execute(request, options);
                                } catch (IOException e) {
                                    log.error("Helix API call execution failed", e);
                                    return null;
                                }
                            })
                            .get(timeout, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        log.error("Throttled Helix API call timed-out before completion", e);
                        return null;
                    }
                }
            }
        }

        // Fallback: just run the http request
        return client.execute(request, options);
    }

}
