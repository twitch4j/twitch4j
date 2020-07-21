package com.github.twitch4j.common.util;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread-safe, configurable helper for applying the exponential backoff algorithm with optional jitter and/or truncation.
 */
@Value
@Builder(toBuilder = true)
public class ExponentialBackoffStrategy {

    /**
     * The maximum backoff value (on average), in milliseconds.
     * If set to a negative value, the algorithm will not be of the truncated variety.
     */
    @Builder.Default
    long maximumBackoff = Duration.ofMinutes(2).toMillis();

    /**
     * The multiplier on back-offs that is in the base of the exponent.
     * <p>
     * The default is 2, which results in doubling of average delays with additional failures.
     * This generally should be set to a value greater than 1 so that delays tend to increase with more failures.
     */
    @Builder.Default
    double multiplier = 2.0;

    /**
     * Whether the first attempt after a failure should take place without delay.
     * <p>
     * To avoid a "stampeding herd" of reconnecting clients, {@link ExponentialBackoffStrategyBuilder#jitter(boolean)} can be enabled
     * and {@link ExponentialBackoffStrategyBuilder#initialJitterRange(long)} can optionally be configured.
     */
    @Builder.Default
    boolean immediateFirst = true;

    /**
     * Whether (pseudo-)randomness should be applied when computing the exponential backoff.
     * <p>
     * Highly useful for avoiding the <a href="https://en.wikipedia.org/wiki/Thundering_herd_problem">thundering herd problem</a>.
     */
    @Builder.Default
    boolean jitter = true;

    /**
     * The range of initial jitter amounts (in milliseconds) for when both {@link #isImmediateFirst()} and {@link #isJitter()} are true.
     */
    @Builder.Default
    long initialJitterRange = Duration.ofSeconds(5).toMillis();

    /**
     * The milliseconds value for the first non-zero backoff.
     * When {@link #isJitter()} is true, this becomes an average targeted value rather than a strictly enforced constant.
     */
    @Builder.Default
    long baseMillis = Duration.ofSeconds(1).toMillis();

    /**
     * The maximum number of retries that should be allowed.
     * <p>
     * A negative value corresponds to no limit.
     * A zero value corresponds to no retries allowed.
     * A positive value enforces a specific maximum.
     */
    @Builder.Default
    int maxRetries = -1;

    /**
     * The number of consecutive failures that have occurred.
     */
    @Getter(value = AccessLevel.PROTECTED)
    AtomicInteger failures = new AtomicInteger();

    /**
     * Sleeps for the delay suggested by {@link #get()}.
     *
     * @return whether the sleep was successful (could be false if maximum attempts have been hit, for example).
     */
    public boolean sleep() {
        final long millis = this.get();

        if (millis < 0)
            return false;

        if (millis > 0)
            try {
                Thread.sleep(millis);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }

        return true;
    }

    /**
     * Increments the failure count and computes the appropriate exponential backoff.
     *
     * @return the amount of milliseconds to delay before retrying.
     */
    public long get() {
        // Atomically increment failures
        int f = failures.getAndIncrement();

        // Check if the maximum allowed retries have been hit
        // noinspection ConstantConditions (does not properly understand @Builder.Default)
        if (maxRetries >= 0 && f >= maxRetries)
            return -1L;

        // Allow for an initial retry attempt with minimal delay
        if (immediateFirst) {
            if (f == 0) {
                if (jitter)
                    return ThreadLocalRandom.current().nextLong(initialJitterRange);

                return 0L;
            }

            f -= 1; // fix exponent in formula below
        }

        // Calculate exponential backoff
        double delay = Math.pow(multiplier, f) * baseMillis;

        // Truncate if desired
        // To minimize herding, this takes place before the application of jitter.
        // The trade-off is: 50% of delays generated at the maximum will exceed the specified maximum
        // while 50% will be lower, in aggregate, resulting in an average conditional delay that is exactly the maximum.
        if (maximumBackoff >= 0)
            delay = Math.min(delay, maximumBackoff);

        // Simple approach to apply jitter in a way that maintains the same exponential characteristic while minimizing herding
        // To minimize herding, we sample from a uniform random distribution for maximum entropy
        // Proof: https://kconrad.math.uconn.edu/blurbs/analysis/entropypost.pdf
        // Practical note: we do not use SecureRandom as a way to squeeze out more entropy as the performance hit is not warranted in this context.
        // To maintain the same average exponential delay, we multiply it by 2 before sending it to the distribution
        // Proof: X = Unif(0,1) ; E[X] = 0.5 ; E[2 * delay * X] = 2 * delay * 0.5 = delay ; QED
        // Practical note: In pursuit of these properties, this implementation allows for consecutive delays to not necessarily be monotonically increasing,
        // which may not be desirable in certain circumstances, and, if unlucky, this algorithm could lead to longer wait times than others.
        if (jitter && delay != 0) {
            delay *= 2;
            delay *= ThreadLocalRandom.current().nextDouble();
        }

        // Conform to long
        return Math.round(delay);
    }

    /**
     * Resets the failure count for exponential backoff calculations.
     */
    public void reset() {
        this.failures.set(0);
    }

    /**
     * @return a new {@link ExponentialBackoffStrategy} instance with the same configuration settings (and no failures).
     */
    public ExponentialBackoffStrategy copy() {
        return this.toBuilder().build();
    }
}
