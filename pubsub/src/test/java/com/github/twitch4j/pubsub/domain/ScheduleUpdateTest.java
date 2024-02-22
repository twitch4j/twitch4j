package com.github.twitch4j.pubsub.domain;

import com.github.twitch4j.common.util.TypeConvert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unittest")
class ScheduleUpdateTest {

    @Test
    void deserializeSnooze() {
        String json = "{\"type\":\"ads-schedule-update\",\"scheduleUpdateType\":\"Snooze\",\"adSchedule\":[{\"runAtTime\":\"2023-07-20T16:30:45Z\",\"durationSeconds\":60}]}";
        ScheduleUpdate data = TypeConvert.jsonToObject(json, ScheduleUpdate.class);
        assertNotNull(data);
        assertTrue(data.isSnooze());
        assertNotNull(data.getAdSchedule());
        assertFalse(data.getAdSchedule().isEmpty());
        ScheduledAd ad = data.getAdSchedule().get(0);
        assertNotNull(ad);
        assertEquals(60L, ad.getDurationSeconds());
        assertEquals(Instant.parse("2023-07-20T16:30:45Z"), ad.getRunAtTime());
    }

}
