package com.github.twitch4j.helix.interceptor;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.enums.TwitchLimitType;
import com.github.twitch4j.common.util.BucketUtils;
import com.github.twitch4j.common.util.TwitchLimitRegistry;
import com.github.twitch4j.helix.domain.SendPubSubMessageInput;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@RequiredArgsConstructor
@SuppressWarnings({ "ConstantConditions", "unused" })
public final class TwitchHelixRateLimitTracker {

    private static final String AUTOMOD_STATUS_MINUTE_ID = TwitchLimitType.HELIX_AUTOMOD_STATUS_LIMIT + "-min";
    private static final String AUTOMOD_STATUS_HOUR_ID = TwitchLimitType.HELIX_AUTOMOD_STATUS_LIMIT + "-hr";

    /**
     * @see TwitchLimitType#HELIX_AUTOMOD_STATUS_LIMIT
     */
    public static final List<Bandwidth> AUTOMOD_STATUS_NORMAL_BANDWIDTH = Arrays.asList(
        Bandwidth.simple(5, Duration.ofMinutes(1L)).withId(AUTOMOD_STATUS_MINUTE_ID),
        Bandwidth.simple(50, Duration.ofHours(1L)).withId(AUTOMOD_STATUS_HOUR_ID)
    );

    /**
     * @see TwitchLimitType#HELIX_AUTOMOD_STATUS_LIMIT
     */
    public static final List<Bandwidth> AUTOMOD_STATUS_AFFILIATE_BANDWIDTH = Arrays.asList(
        Bandwidth.simple(10, Duration.ofMinutes(1L)).withId(AUTOMOD_STATUS_MINUTE_ID),
        Bandwidth.simple(100, Duration.ofHours(1L)).withId(AUTOMOD_STATUS_HOUR_ID)
    );

    /**
     * @see TwitchLimitType#HELIX_AUTOMOD_STATUS_LIMIT
     */
    public static final List<Bandwidth> AUTOMOD_STATUS_PARTNER_BANDWIDTH = Arrays.asList(
        Bandwidth.simple(30, Duration.ofMinutes(1L)).withId(AUTOMOD_STATUS_MINUTE_ID),
        Bandwidth.simple(300, Duration.ofHours(1L)).withId(AUTOMOD_STATUS_HOUR_ID)
    );

    /**
     * Officially documented rate limit for {@link com.github.twitch4j.helix.TwitchHelix#startRaid(String, String, String)} and {@link com.github.twitch4j.helix.TwitchHelix#cancelRaid(String, String)}
     */
    private static final Bandwidth RAIDS_BANDWIDTH = Bandwidth.simple(10, Duration.ofMinutes(10));

    /**
     * Officially documented per-channel rate limit on {@link com.github.twitch4j.helix.TwitchHelix#sendExtensionChatMessage(String, String, String, String, String)}
     */
    private static final Bandwidth EXT_CHAT_BANDWIDTH = Bandwidth.simple(12, Duration.ofMinutes(1L));

    /**
     * Officially documented bucket size (but unofficial refill rate) for {@link com.github.twitch4j.helix.TwitchHelix#sendExtensionPubSubMessage(String, String, SendPubSubMessageInput)}
     *
     * @see <a href="https://github.com/twitchdev/issues/issues/612">Issue report</a>
     */
    private static final Bandwidth EXT_PUBSUB_BANDWIDTH = Bandwidth.classic(100, Refill.greedy(1, Duration.ofSeconds(1L)));

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
        .expireAfterAccess(80, TimeUnit.SECONDS)
        .build();

    /**
     * Extensions API: send chat message rate limit buckets per channel
     */
    private final Cache<String, Bucket> extensionChatBuckets = Caffeine.newBuilder()
        .expireAfterAccess(1, TimeUnit.MINUTES)
        .build();

    /**
     * Extensions API: send pubsub message rate limit buckets per channel
     */
    private final Cache<String, Bucket> extensionPubSubBuckets = Caffeine.newBuilder()
        .expireAfterAccess(100, TimeUnit.SECONDS)
        .build();

    /**
     * Raids API: start and cancel raid rate limit buckets per channel
     */
    private final Cache<String, Bucket> raidsByChannelId = Caffeine.newBuilder()
        .expireAfterAccess(10, TimeUnit.MINUTES)
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
     * The primary (global helix) rate limit bandwidth to use
     */
    private final Bandwidth apiRateLimit;

    /**
     * Twitch Helix Token Manager
     */
    private final TwitchHelixTokenManager tokenManager;

    /*
     * Primary (global helix) rate limit bucket finder
     */

    @NotNull
    Bucket getOrInitializeBucket(@NotNull String key) {
        return primaryBuckets.get(key, k -> BucketUtils.createBucket(this.apiRateLimit));
    }

    @NotNull
    String getPrimaryBucketKey(@NotNull OAuth2Credential credential) {
        // App access tokens share the same bucket for a given client id
        // User access tokens share the same bucket for a given client id & user id pair
        // For this method to work, credential must have been augmented with information from getAdditionalCredentialInformation (which is done by the interceptor)
        // Thus, this logic yields the key that is associated with each primary helix bucket
        String clientId = TwitchHelixTokenManager.extractClientId(credential);
        return clientId == null ? "" : StringUtils.isEmpty(credential.getUserId()) ? clientId : clientId + "-" + credential.getUserId();
    }

    /*
     * Secondary (endpoint-specific) rate limit buckets
     */

    @NotNull
    Bucket getExtensionChatBucket(@NotNull String clientId, @NotNull String channelId) {
        return extensionChatBuckets.get(clientId + ':' + channelId, k -> BucketUtils.createBucket(EXT_CHAT_BANDWIDTH));
    }

    @NotNull
    Bucket getExtensionPubSubBucket(@NotNull String clientId, @NotNull String channelId) {
        return extensionPubSubBuckets.get(clientId + ':' + channelId, k -> BucketUtils.createBucket(EXT_PUBSUB_BANDWIDTH));
    }

    @NotNull
    Bucket getAutomodStatusBucket(@NotNull String channelId) {
        return TwitchLimitRegistry.getInstance().getOrInitializeBucket(channelId, TwitchLimitType.HELIX_AUTOMOD_STATUS_LIMIT, AUTOMOD_STATUS_NORMAL_BANDWIDTH);
    }

    @NotNull
    Bucket getRaidsBucket(@NotNull String channelId) {
        return raidsByChannelId.get(channelId, k -> BucketUtils.createBucket(RAIDS_BANDWIDTH));
    }

    @NotNull
    @Unofficial
    Bucket getModerationBucket(@NotNull String channelId) {
        return bansByChannelId.get(channelId, k -> BucketUtils.createBucket(BANS_BANDWIDTH));
    }

    @NotNull
    @Unofficial
    Bucket getClipBucket(@NotNull String userId) {
        return clipsByUserId.get(userId, k -> BucketUtils.createBucket(CLIPS_BANDWIDTH));
    }

    @NotNull
    @Unofficial
    Bucket getTermsBucket(@NotNull String channelId) {
        return termsByChannelId.get(channelId, k -> BucketUtils.createBucket(TERMS_BANDWIDTH));
    }

    /*
     * Methods to conservatively update remaining points in rate limit buckets, based on incoming twitch statistics
     */

    public void updateRemaining(@NotNull String token, int remaining) {
        this.updateRemainingGeneric(token, remaining, this::getPrimaryBucketKey, this::getOrInitializeBucket);
    }

    public void updateRemainingExtensionChat(@NotNull String clientId, @NotNull String channelId, int remaining) {
        this.updateRemainingConservative(getExtensionChatBucket(clientId, channelId), remaining);
    }

    public void updateRemainingExtensionPubSub(@NotNull String clientId, @NotNull String target, int remaining) {
        this.updateRemainingConservative(getExtensionPubSubBucket(clientId, target), remaining);
    }

    public void updateRemainingCreateClip(@NotNull String token, int remaining) {
        this.updateRemainingGeneric(token, remaining, OAuth2Credential::getUserId, this::getClipBucket);
    }

    @Unofficial
    public void markDepletedBanBucket(@NotNull String channelId) {
        // Called upon a 429 for banUser or unbanUser
        Bucket modBucket = this.getModerationBucket(channelId);
        modBucket.consumeIgnoringRateLimits(Math.max(modBucket.tryConsumeAsMuchAsPossible(), 1)); // intentionally go negative to induce a pause
    }

    private void updateRemainingGeneric(String token, int remaining, Function<OAuth2Credential, String> credToKey, Function<String, Bucket> keyToBucket) {
        OAuth2Credential credential = tokenManager.getIfPresent(token);
        if (credential == null) return;

        String key = credToKey.apply(credential);
        if (key == null) return;

        Bucket bucket = keyToBucket.apply(key);
        updateRemainingConservative(bucket, remaining);
    }

    private void updateRemainingConservative(Bucket bucket, int remaining) {
        long diff = bucket.getAvailableTokens() - remaining;
        if (diff > 0) bucket.tryConsumeAsMuchAsPossible(diff);
    }

}
