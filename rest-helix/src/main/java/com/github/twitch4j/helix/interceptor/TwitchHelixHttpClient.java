package com.github.twitch4j.helix.interceptor;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.common.util.BucketUtils;
import feign.Client;
import feign.Request;
import feign.Response;
import feign.okhttp.OkHttpClient;
import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.Callable;
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
    private final TwitchHelixTokenManager tokenManager;
    private final TwitchHelixRateLimitTracker rateLimitTracker;
    private final long timeout;

    public TwitchHelixHttpClient(OkHttpClient client, ScheduledThreadPoolExecutor executor, TwitchHelixTokenManager tokenManager, TwitchHelixRateLimitTracker rateLimitTracker, Integer timeout) {
        this.client = client;
        this.executor = executor;
        this.tokenManager = tokenManager;
        this.rateLimitTracker = rateLimitTracker;
        this.timeout = timeout == null ? 60 * 1000 : timeout.longValue();
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        // Check whether this request should be delayed to conform to rate limits
        String token = singleFirst(request.headers().get(AUTH_HEADER));
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            OAuth2Credential credential = tokenManager.getIfPresent(token.substring(BEARER_PREFIX.length()));
            if (credential != null) {
                // First consume from helix global rate limit (800/min by default)
                Bucket bucket = rateLimitTracker.getOrInitializeBucket(rateLimitTracker.getPrimaryBucketKey(credential));
                return executeAgainstBucket(bucket, () -> delegatedExecute(request, options));
            }
        }

        // Fallback: just run the http request
        return delegatedExecute(request, options);
    }

    /**
     * After the helix rate limit has been evaluated, check for any other endpoint-specific limits before actually executing the request.
     *
     * @param request feign request
     * @param options feign request options
     * @return feign response
     * @throws IOException on network errors
     */
    private Response delegatedExecute(Request request, Request.Options options) throws IOException {
        String templatePath = request.requestTemplate().path();

        // Moderation API: banUser and unbanUser share a bucket per channel id
        if (templatePath.endsWith("/moderation/bans")) {
            // Obtain the channel id
            String channelId = request.requestTemplate().queries().getOrDefault("broadcaster_id", Collections.emptyList()).iterator().next();

            // Conform to endpoint-specific bucket
            Bucket modBucket = rateLimitTracker.getModerationBucket(channelId);
            return executeAgainstBucket(modBucket, () -> client.execute(request, options));
        }

        // Moderation API: addBlockedTerm and removeBlockedTerm share a bucket per channel id
        if (templatePath.endsWith("/moderation/blocked_terms") && (request.httpMethod() == Request.HttpMethod.POST || request.httpMethod() == Request.HttpMethod.DELETE)) {
            // Obtain the channel id
            String channelId = request.requestTemplate().queries().getOrDefault("broadcaster_id", Collections.emptyList()).iterator().next();

            // Conform to endpoint-specific bucket
            Bucket termsBucket = rateLimitTracker.getTermsBucket(channelId);
            return executeAgainstBucket(termsBucket, () -> client.execute(request, options));
        }

        // Clips API: createClip has a stricter bucket that applies per user id
        if (request.httpMethod() == Request.HttpMethod.POST && templatePath.endsWith("/clips")) {
            // Obtain user id
            String token = request.headers().get(AUTH_HEADER).iterator().next().substring(BEARER_PREFIX.length());
            OAuth2Credential cred = tokenManager.getIfPresent(token);
            String userId = cred != null ? cred.getUserId() : "";

            // Conform to endpoint-specific bucket
            Bucket clipBucket = rateLimitTracker.getClipBucket(userId != null ? userId : "");
            return executeAgainstBucket(clipBucket, () -> client.execute(request, options));
        }

        // Raids API: startRaid and cancelRaid have a stricter bucket that applies per channel id
        if (templatePath.endsWith("/raids")) {
            // Obtain the channel id
            String param = request.httpMethod() == Request.HttpMethod.POST ? "from_broadcaster_id" : "broadcaster_id";
            String channelId = request.requestTemplate().queries().getOrDefault(param, Collections.emptyList()).iterator().next();

            // Conform to endpoint-specific bucket
            Bucket raidBucket = rateLimitTracker.getRaidsBucket(channelId);
            return executeAgainstBucket(raidBucket, () -> client.execute(request, options));
        }

        // no endpoint-specific rate limiting was needed; simply perform network request now
        return client.execute(request, options);
    }

    private <T> T executeAgainstBucket(Bucket bucket, Callable<T> call) throws IOException {
        try {
            return BucketUtils.scheduleAgainstBucket(bucket, executor, call).get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            if (e.getCause() instanceof IOException) throw (IOException) e.getCause();
            log.error("Throttled Helix API call timed-out before completion", e);
            return null;
        }
    }

}
