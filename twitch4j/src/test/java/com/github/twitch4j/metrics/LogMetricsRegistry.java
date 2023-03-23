package com.github.twitch4j.metrics;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingRegistryConfig;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

/**
 * a metrics registry that logs the current metrics every 10s for testing purposes
 */
public class LogMetricsRegistry extends LoggingMeterRegistry {

    public LogMetricsRegistry() {
        super(new LoggingRegistryConfig() {
            @Override
            public String get(String key) {
                return null;
            }

            @Override
            public @NotNull Duration step() {
                return Duration.ofSeconds(10);
            }
        }, Clock.SYSTEM);
    }

}
