package com.github.twitch4j.helix.interceptor;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.util.BucketUtils;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@RequiredArgsConstructor
public class TwitchHelixRateLimitTracker {

    /**
     * Empirically determined rate limit on helix bans and unbans, per channel
     */
    @Unofficial
    private static final Bandwidth BANS_BANDWIDTH = Bandwidth.simple(100, Duration.ofSeconds(30));

    /**
     * Empirically determined rate limit on the helix create clip endpoint, per user
     */
    @Unofficial
    private static final Bandwidth CLIPS_BANDWIDTH = Bandwidth.simple(600, Duration.ofSeconds(60));

    /**
     * Empirically determined rate limit on helix add and remove block term, per channel
     */
    @Unofficial
    private static final Bandwidth TERMS_BANDWIDTH = Bandwidth.simple(60, Duration.ofSeconds(60));

    /**
     * Rate limit buckets by user/app
     */
    private final Cache<String, Bucket> primaryBuckets = Caffeine.newBuilder()
        .expireAfterAccess(1, TimeUnit.MINUTES)
        .build();

    /**
     * Moderation API: ban and unban rate limit buckets per channel
     */
    private final Cache<String, Bucket> bansByChannelId = Caffeine.newBuilder()
        .expireAfterAccess(1, TimeUnit.MINUTES)
        .build();

    /**
     * Create Clip API rate limit buckets per user
     */
    private final Cache<String, Bucket> clipsByUserId = Caffeine.newBuilder()
        .expireAfterAccess(1, TimeUnit.MINUTES)
        .build();

    /**
     * Moderation API: add and remove blocked term rate limit buckets per channel
     */
    private final Cache<String, Bucket> termsByChannelId = Caffeine.newBuilder()
        .expireAfterAccess(1, TimeUnit.MINUTES)
        .build();

    /**
     * Twitch Helix Interceptor
     */
    private final TwitchHelixClientIdInterceptor interceptor; // provided by RequiredArgsConstructor

    /*
     * Primary (global helix) rate limit bucket finder
     */

    protected Bucket getOrInitializeBucket(String key) {
        return primaryBuckets.get(key, k -> BucketUtils.createBucket(interceptor.getApiRateLimit()));
    }

    protected String getKey(OAuth2Credential credential) {
        // App access tokens share the same bucket for a given client id
        // User access tokens share the same bucket for a given client id & user id pair
        // For this method to work, credential must have been augmented with information from getAdditionalCredentialInformation (which is done by the interceptor)
        // Thus, this logic yields the key that is associated with each primary helix bucket
        String clientId = (String) credential.getContext().get("client_id");
        return clientId == null ? null : credential.getUserId() == null ? clientId : clientId + "-" + credential.getUserId();
    }

    /*
     * Secondary (endpoint-specific) rate limit buckets
     */

    @Unofficial
    protected Bucket getModerationBucket(String channelId) {
        return bansByChannelId.get(channelId, k -> BucketUtils.createBucket(BANS_BANDWIDTH));
    }

    @Unofficial
    protected Bucket getClipBucket(String userId) {
        return clipsByUserId.get(userId, k -> BucketUtils.createBucket(CLIPS_BANDWIDTH));
    }

    @Unofficial
    protected Bucket getTermsBucket(String channelId) {
        return termsByChannelId.get(channelId, k -> BucketUtils.createBucket(TERMS_BANDWIDTH));
    }

    /*
     * Methods to conservatively update remaining points in rate limit buckets, based on incoming twitch statistics
     */

    public void updateRemaining(String token, int remaining) {
        this.updateRemainingGeneric(token, remaining, this::getKey, this::getOrInitializeBucket);
    }

    public void updateRemainingCreateClip(String token, int remaining) {
        this.updateRemainingGeneric(token, remaining, OAuth2Credential::getUserId, this::getClipBucket);
    }

    public void markDepletedBanBucket(String channelId) {
        // Called upon a 429 for banUser or unbanUser
        Bucket modBucket = this.getModerationBucket(channelId);
        modBucket.consumeIgnoringRateLimits(Math.max(modBucket.tryConsumeAsMuchAsPossible(), 1)); // intentionally go negative to induce a pause
    }

    private void updateRemainingGeneric(String token, int remaining, Function<OAuth2Credential, String> credToKey, Function<String, Bucket> keyToBucket) {
        OAuth2Credential credential = interceptor.getAccessTokenCache().getIfPresent(token);
        if (credential == null) return;

        String key = credToKey.apply(credential);
        if (key == null) return;

        Bucket bucket = keyToBucket.apply(key);
        long diff = bucket.getAvailableTokens() - remaining;
        if (diff > 0) bucket.tryConsumeAsMuchAsPossible(diff);
    }

}
