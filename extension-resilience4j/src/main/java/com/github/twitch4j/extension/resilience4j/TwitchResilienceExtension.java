package com.github.twitch4j.extension.resilience4j;

import com.github.twitch4j.api.ThrowingFunction;
import com.github.twitch4j.api.TwitchExtension;
import feign.InvocationHandlerFactory;
import feign.Target;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.functions.CheckedFunction;
import io.github.resilience4j.micrometer.tagged.TaggedBulkheadMetrics;
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics;
import io.github.resilience4j.micrometer.tagged.TaggedRateLimiterMetrics;
import io.github.resilience4j.micrometer.tagged.TaggedRetryMetrics;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Builder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.resilience4j.ratelimiter.RateLimiter.waitForPermission;

/**
 * Resilience4J Module
 * <p>
 * This module adds the Resilience4J capabilities to the feign client used by Twitch4J.
 */
@ApiStatus.Experimental
public class TwitchResilienceExtension implements TwitchExtension {
    private final BulkheadRegistry bulkheadRegistry;
    private final RateLimiterRegistry rateLimiterRegistry;
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final RetryRegistry retryRegistry;
    private final ConcurrentHashMap<String, Bulkhead> bulkheadCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, RateLimiter> rateLimiterCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CircuitBreaker> circuitBreakerCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Retry> retryCache = new ConcurrentHashMap<>();
    private final MeterRegistry meterRegistry;

    public TwitchResilienceExtension() {
        this(null, null, null, null, null);
    }

    @Builder
    public TwitchResilienceExtension(MeterRegistry meterRegistry, BulkheadConfig bulkheadConfig, RateLimiterConfig rateLimiterConfig, CircuitBreakerConfig circuitBreakerConfig, RetryConfig retryConfig) {
        this.meterRegistry = meterRegistry;

        // custom configurations
        bulkheadRegistry = BulkheadRegistry.of(Optional.ofNullable(bulkheadConfig).orElse(BulkheadConfig.ofDefaults()));
        rateLimiterRegistry = RateLimiterRegistry.of(Optional.ofNullable(rateLimiterConfig).orElse(RateLimiterConfig.ofDefaults()));
        circuitBreakerRegistry = CircuitBreakerRegistry.of(Optional.ofNullable(circuitBreakerConfig).orElse(CircuitBreakerConfig.ofDefaults()));
        retryRegistry = RetryRegistry.of(Optional.ofNullable(retryConfig).orElse(RetryConfig.ofDefaults()));

        // metrics
        if (meterRegistry != null) {
            TaggedBulkheadMetrics.ofBulkheadRegistry(bulkheadRegistry).bindTo(meterRegistry);
            TaggedRateLimiterMetrics.ofRateLimiterRegistry(rateLimiterRegistry).bindTo(meterRegistry);
            TaggedCircuitBreakerMetrics.ofCircuitBreakerRegistry(circuitBreakerRegistry).bindTo(meterRegistry);
            TaggedRetryMetrics.ofRetryRegistry(retryRegistry).bindTo(meterRegistry);
        }
    }

    @Override
    public @NotNull ThrowingFunction<Object[], Object, Throwable> decorateFeignInvocation(String backendName, ThrowingFunction<Object[], Object, Throwable> function, Map<Method, InvocationHandlerFactory.MethodHandler> dispatch, Target<?> target) {
        // get or create Resilience4J instances for current backend
        Bulkhead bulkhead = bulkheadCache.computeIfAbsent(backendName, bulkheadRegistry::bulkhead);
        RateLimiter rateLimiter = rateLimiterCache.computeIfAbsent(backendName, rateLimiterRegistry::rateLimiter);
        CircuitBreaker circuitBreaker = circuitBreakerCache.computeIfAbsent(backendName, circuitBreakerRegistry::circuitBreaker);
        Retry retry = retryCache.computeIfAbsent(backendName, retryRegistry::retry);

        // decorate function (inverse order of execution)
        function = decorateBulkhead(bulkhead, function);
        function = decorateRateLimiter(rateLimiter, 1, function);
        function = decorateCircuitBreaker(circuitBreaker, function);
        function = decorateRetry(retry, function);

        return function;
    }

    /**
     * Returns a function which is decorated by a Bulkhead.
     * <p>
     * See {@link Bulkhead#decorateCheckedFunction(Bulkhead, CheckedFunction)}.
     */
    private <T, R> ThrowingFunction<T, R, Throwable> decorateBulkhead(Bulkhead bulkhead, ThrowingFunction<T, R, Throwable> function) {
        return (T t) -> {
            bulkhead.acquirePermission();
            try {
                return function.apply(t);
            } finally {
                bulkhead.onComplete();
            }
        };
    }

    /**
     * Returns a function which is decorated by a RateLimiter.
     * <p>
     * See {@link RateLimiter#decorateCheckedFunction(RateLimiter, int, CheckedFunction)}.
     */
    private <T, R> ThrowingFunction<T, R, Throwable> decorateRateLimiter(RateLimiter rateLimiter, int permits, ThrowingFunction<T, R, Throwable> function) {
        return (T t) -> {
            waitForPermission(rateLimiter, permits);
            try {
                R result = function.apply(t);
                rateLimiter.onResult(result);
                return result;
            } catch (Exception exception) {
                rateLimiter.onError(exception);
                throw exception;
            }
        };
    }

    /**
     * Returns a function which is decorated by a CircuitBreaker.
     * <p>
     * See {@link CircuitBreaker#decorateCheckedFunction(CircuitBreaker, CheckedFunction)}.
     */
    private <T, R> ThrowingFunction<T, R, Throwable> decorateCircuitBreaker(CircuitBreaker circuitBreaker, ThrowingFunction<T, R, Throwable> function) {
        return (T t) -> {
            circuitBreaker.acquirePermission();
            final long start = circuitBreaker.getCurrentTimestamp();
            try {
                R result = function.apply(t);
                long duration = circuitBreaker.getCurrentTimestamp() - start;
                circuitBreaker.onResult(duration, circuitBreaker.getTimestampUnit(), result);
                return result;
            } catch (Exception exception) {
                long duration = circuitBreaker.getCurrentTimestamp() - start;
                circuitBreaker.onError(duration, circuitBreaker.getTimestampUnit(), exception);
                throw exception;
            }
        };
    }

    /**
     * Returns a function which is decorated by a Retry.
     * <p>
     * See {@link Retry#decorateCheckedFunction(Retry, CheckedFunction)}.
     */
    private <T, R> ThrowingFunction<T, R, Throwable> decorateRetry(Retry retry, ThrowingFunction<T, R, Throwable> function) {
        return (T t) -> {
            Retry.Context<R> context = retry.context();
            do {
                try {
                    R result = function.apply(t);
                    final boolean validationOfResult = context.onResult(result);
                    if (!validationOfResult) {
                        context.onComplete();
                        return result;
                    }
                } catch (Exception exception) {
                    context.onError(exception);
                }
            } while (true);
        };
    }
}
