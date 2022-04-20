package com.github.twitch4j.common.util;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.local.LocalBucketBuilder;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;

public class BucketUtils {

    /**
     * Creates a bucket with the specified bandwidth.
     *
     * @param limit the bandwidth
     * @return the bucket
     */
    public static Bucket createBucket(Bandwidth limit) {
        return Bucket.builder().addLimit(limit).build();
    }

    /**
     * Creates a bucket with the specified bandwidths.
     *
     * @param limits the bandwidths
     * @return the bucket
     */
    public static Bucket createBucket(Bandwidth... limits) {
        LocalBucketBuilder builder = Bucket.builder();
        for (Bandwidth limit : limits) {
            builder.addLimit(limit);
        }
        return builder.build();
    }

    /**
     * Creates a bucket with the specified bandwidths.
     *
     * @param limits the bandwidths
     * @return the bucket
     */
    public static Bucket createBucket(Iterable<Bandwidth> limits) {
        LocalBucketBuilder builder = Bucket.builder();
        for (Bandwidth limit : limits) {
            builder.addLimit(limit);
        }
        return builder.build();
    }

    /**
     * Performs the callable after a token has been consumed from the bucket using the executor.
     * <p>
     * Note: ExecutionException should be inspected if the passed action can throw an exception.
     *
     * @param bucket   rate limit bucket
     * @param executor scheduled executor service for async calls
     * @param call     task that requires a bucket point
     * @return the future result of the call
     */
    public static <T> CompletableFuture<T> scheduleAgainstBucket(Bucket bucket, ScheduledExecutorService executor, Callable<T> call) {
        if (bucket.tryConsume(1L))
            return CompletableFuture.supplyAsync(new SneakySupplier<>(call));

        return bucket.asScheduler().consume(1L, executor).thenApplyAsync(v -> new SneakySupplier<>(call).get());
    }

    /**
     * Runs the action after a token has been consumed from the bucket using the executor.
     * <p>
     * Note: while the executor is used to consume the bucket token, the action is performed on the fork-join common pool, by default.
     *
     * @param bucket   rate limit bucket
     * @param executor scheduled executor service for async calls
     * @param action   runnable that requires a bucket point
     * @return a future to track completion progress
     */
    public static CompletableFuture<Void> scheduleAgainstBucket(Bucket bucket, ScheduledExecutorService executor, Runnable action) {
        if (bucket.tryConsume(1L))
            return CompletableFuture.runAsync(action);

        return bucket.asScheduler().consume(1L, executor).thenRunAsync(action);
    }

}
