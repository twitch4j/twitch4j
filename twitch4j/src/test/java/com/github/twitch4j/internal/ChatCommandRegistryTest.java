package com.github.twitch4j.internal;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.github.twitch4j.internal.ChatCommandRegistry.parseDuration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
@Tag("unittest")
class ChatCommandRegistryTest {

    @Test
    @DisplayName("Test ChatCommandRegistry#parseDuration yields expected values")
    void testDurationParsing() {
        // blank input => 0
        assertEquals(0, parseDuration(""));
        assertEquals(0, parseDuration(" "));
        assertEquals(0, parseDuration("   "));

        // bad input => null
        assertNull(parseDuration("o"));
        assertNull(parseDuration("6q"));

        // no unit => seconds
        assertEquals(69, parseDuration("69"));
        assertEquals(70, parseDuration("70    "));
        assertEquals(71, parseDuration("    71"));
        assertEquals(72, parseDuration("072"));

        // fully abbreviated
        assertEquals(69, parseDuration("1m9s"));
        assertEquals(70, parseDuration("1m 10s"));
        assertEquals(71, parseDuration("1m   11s"));
        assertEquals(72, parseDuration("1m   12s   "));
        assertEquals(73, parseDuration("   1m   13s   "));
        assertEquals(74, parseDuration("1m   14"));
        assertEquals(74, parseDuration("1  m   14"));
        assertEquals(3666, parseDuration("1h66s"));
        assertEquals(420, parseDuration("7m"));
        assertEquals(86420, parseDuration("1d 20s"));
        assertEquals(690000, parseDuration("1w 1420m"));
        assertEquals(2420000, parseDuration("1mo 800s"));

        // partially abbreviated
        assertEquals(69, parseDuration("1min 9s"));
        assertEquals(70, parseDuration("1min 10sec"));
        assertEquals(3666, parseDuration("1hr 66s"));
        assertEquals(420, parseDuration("7 mins"));
        assertEquals(86420, parseDuration("1day 20secs"));
        assertEquals(690000, parseDuration("1wk 1420m"));
        assertEquals(2420000, parseDuration("1 mo 800 seconds"));

        // not abbreviated
        assertEquals(69, parseDuration("1minute 9seconds"));
        assertEquals(70, parseDuration("1 minute 10 seconds"));
        assertEquals(3666, parseDuration("1 hour 66 seconds"));
        assertEquals(420, parseDuration("7 minutes"));
        assertEquals(86420, parseDuration("1 day 20 seconds"));
        assertEquals(690000, parseDuration("1 week 1420 minutes"));
        assertEquals(2420000, parseDuration("1 month 800 seconds"));

        // unordered
        assertEquals(69, parseDuration("9s 1m"));

        // many units
        assertEquals(
            (60 * 60 * 24 * 7) + 2 * (60 * 60 * 24) + 3 * (60 * 60) + 4 * 60 + 5,
            parseDuration("1wk 2days 3hr 4min 5s")
        );

        // repeated units
        assertEquals(420, parseDuration("6m 1m"));
        assertEquals(666, parseDuration("10m 6s 1m"));
    }

}
