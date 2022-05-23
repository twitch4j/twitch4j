package com.github.twitch4j.common.util;

import com.github.twitch4j.common.enums.TwitchLimitType;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.TokensInheritanceStrategy;
import lombok.NonNull;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This singleton facilitates sharing of key rate-limit buckets by user and limit type.
 * <p>
 * For example:
 * <pre>
 * {@code TwitchLimitRegistry.getInstance().setLimit(
 *         "149223493",
 *         TwitchLimitType.CHAT_MESSAGE_LIMIT,
 *         Collections.singletonList(TwitchChatLimitHelper.MOD_MESSAGE_LIMIT)
 *     );
 * }
 * </pre>
 */
public enum TwitchLimitRegistry {

    /**
     * The single thread-safe instance of the limit registry.
     */
    INSTANCE;

    private final Map<String, Map<TwitchLimitType, Bucket>> limits = new ConcurrentHashMap<>();

    /**
     * Attempts to set a user's bucket for a specific rate-limit.
     *
     * @param userId    the id of the user whose rate limit is being specified.
     * @param limitType the type of rate-limit that is being configured.
     * @param limit     the bandwidths that are applicable for this user and rate limit type.
     */
    public void setLimit(@NonNull String userId, @NonNull TwitchLimitType limitType, @NonNull List<Bandwidth> limit) {
        getBucketsByUser(userId).compute(limitType, (l, bucket) -> {
            if (bucket != null) {
                bucket.replaceConfiguration(new BucketConfiguration(limit), TokensInheritanceStrategy.AS_IS);
                return bucket;
            }

            return BucketUtils.createBucket(limit);
        });
    }

    /**
     * Invalidates the registered rate limits for this id.
     *
     * @param userId the id of the user or channel whose registered limits can be cleared from memory
     * @return whether the user had registered limits that were cleared
     */
    public boolean invalidateLimitsByUserId(String userId) {
        return limits.remove(userId) != null;
    }

    /**
     * Invalidates a specific rate limit type that was registered for a specific user/channel id.
     *
     * @param userId    the id of the user or channel whose registered limit should be cleared from memory
     * @param limitType the type of rate limit that should be cleared
     * @return whether the user a registered limit of the specified type that could be cleared
     */
    public boolean invalidateLimit(String userId, TwitchLimitType limitType) {
        return limits.getOrDefault(userId, Collections.emptyMap()).remove(limitType) != null;
    }

    /**
     * Obtains the {@link Bucket} for a user and rate limit type, if it has been registered.
     *
     * @param userId    the id of the user whose rate limit bucket is being requested.
     * @param limitType the type of rate limit that is being queried.
     * @return the shared rate limit bucket for this user and limit type, in an optional wrapper
     */
    @NonNull
    public Optional<Bucket> getBucket(@NonNull String userId, @NonNull TwitchLimitType limitType) {
        return Optional.ofNullable(limits.get(userId)).map(buckets -> buckets.get(limitType));
    }

    /**
     * Obtains or creates the {@link Bucket} for a certain user and rate limit type.
     *
     * @param userId    the id of the user in question.
     * @param limitType the type of rate limit in question.
     * @param limit     the default bandwidth settings for this user and rate limit type.
     * @return the shared rate limit bucket for this user and limit type
     */
    @NonNull
    public Bucket getOrInitializeBucket(@NonNull String userId, @NonNull TwitchLimitType limitType, @NonNull List<Bandwidth> limit) {
        return getBucketsByUser(userId).computeIfAbsent(limitType, l -> BucketUtils.createBucket(limit));
    }

    private Map<TwitchLimitType, Bucket> getBucketsByUser(String userId) {
        return limits.computeIfAbsent(userId, s -> Collections.synchronizedMap(new EnumMap<>(TwitchLimitType.class)));
    }

    /**
     * @return the single thread-safe instance of the limit registry.
     */
    @NonNull
    public static TwitchLimitRegistry getInstance() {
        return INSTANCE;
    }

}
