package com.github.twitch4j.common.util;

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
     */
    public static Calendar fromInstant(Instant time) {
        return GregorianCalendar.from(ZonedDateTime.ofInstant(time, ZoneId.systemDefault()));
    }

}
