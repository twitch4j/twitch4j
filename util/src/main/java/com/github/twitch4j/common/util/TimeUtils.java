package com.github.twitch4j.common.util;

import org.jetbrains.annotations.ApiStatus;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimeUtils {

    /**
     * Gets the current time in millis
     *
     * @return long
     */
    public static long getCurrentTimeInMillis() {
        return Instant.now().toEpochMilli();
    }

    /**
     * @param time an {@link Instant}
     * @return a {@link Calendar} instance for the instant, in the system default zone
     * @deprecated Will no longer be used by Twitch4J from version 2.0.0
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    public static Calendar fromInstant(Instant time) {
        return GregorianCalendar.from(ZonedDateTime.ofInstant(time, ZoneId.systemDefault()));
    }

}
