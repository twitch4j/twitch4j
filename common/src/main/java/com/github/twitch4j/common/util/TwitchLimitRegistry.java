package com.github.twitch4j.common.util;

import com.github.twitch4j.common.enums.TwitchLimitType;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.local.LocalBucketBuilder;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public enum TwitchLimitRegistry {

    INSTANCE;

    private final Map<String, Map<TwitchLimitType, Bucket>> limits = new ConcurrentHashMap<>();

    public void setLimit(String userId, TwitchLimitType limitType, List<Bandwidth> limit) {
        getBucketsByUser(userId).compute(limitType, (l, bucket) -> {
            if (bucket != null) {
                bucket.replaceConfiguration(new BucketConfiguration(limit));
                return bucket;
            }

            return constructBucket(limit);
        });
    }

    public Optional<Bucket> getBucket(String userId, TwitchLimitType limitType) {
        return Optional.ofNullable(limits.get(userId)).map(buckets -> buckets.get(limitType));
    }

    public Bucket getOrInitializeBucket(String userId, TwitchLimitType limitType, List<Bandwidth> limit) {
        return getBucketsByUser(userId).computeIfAbsent(limitType, l -> constructBucket(limit));
    }

    private Map<TwitchLimitType, Bucket> getBucketsByUser(String userId) {
        return limits.computeIfAbsent(userId, s -> Collections.synchronizedMap(new EnumMap<>(TwitchLimitType.class)));
    }

    private static Bucket constructBucket(List<Bandwidth> limits) {
        LocalBucketBuilder builder = Bucket4j.builder();
        limits.forEach(builder::addLimit);
        return builder.build();
    }

}
