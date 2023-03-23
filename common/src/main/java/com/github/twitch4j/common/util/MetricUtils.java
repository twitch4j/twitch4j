package com.github.twitch4j.common.util;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class MetricUtils {

    /**
     * @param meterRegistry the configured MeterRegistry, can be null
     * @return the MeterRegistry or a default NOOP CompositeMeterRegistry if meterRegistry is null
     */
    @NotNull
    public static MeterRegistry getMeterRegistry(@Nullable MeterRegistry meterRegistry) {
        if (meterRegistry != null)
            return meterRegistry;

        return new CompositeMeterRegistry(Clock.SYSTEM);
    }

}
