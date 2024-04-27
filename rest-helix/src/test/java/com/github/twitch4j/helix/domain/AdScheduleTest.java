package com.github.twitch4j.helix.domain;

import com.github.twitch4j.common.util.TypeConvert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Tag("unittest")
class AdScheduleTest {

    @Test
    void deserializePartner() {
        // actual example from a partner in early November 2023 (epoch seconds are sent instead of rfc3339 strings)
        AdSchedule ads = TypeConvert.jsonToObject(
            "{\"snooze_count\":3,\"snooze_refresh_at\":0,\"next_ad_at\":1698437870,\"length_seconds\":90,\"last_ad_at\":1698429628,\"preroll_free_time_seconds\":0}",
            AdSchedule.class
        );
        assertNotNull(ads);
        assertEquals(3, ads.getSnoozeCount());
        assertEquals(Instant.ofEpochSecond(1698437870L), ads.getNextAdAt());
        assertEquals(Instant.ofEpochSecond(1698429628L), ads.getLastAdAt());
        assertEquals(90, ads.getLengthSeconds());
        assertEquals(0, ads.getPrerollFreeTimeSeconds());
    }

    @Test
    void deserializeActual() {
        // actual example from 2023-11-03 for a non-affiliate
        AdSchedule ads = TypeConvert.jsonToObject(
            "{\"snooze_count\":3,\"snooze_refresh_at\":0,\"next_ad_at\":0,\"length_seconds\":0,\"last_ad_at\":0,\"preroll_free_time_seconds\":0}",
            AdSchedule.class
        );
        assertNotNull(ads);
        assertEquals(3, ads.getSnoozeCount());
        assertEquals(0, ads.getLengthSeconds());
        assertEquals(0, ads.getPrerollFreeTimeSeconds());
        assertEquals(Instant.EPOCH, ads.getSnoozeRefreshAt());
        assertEquals(Instant.EPOCH, ads.getNextAdAt());
        assertEquals(Instant.EPOCH, ads.getLastAdAt());
    }

    @Test
    void deserializeSpec() {
        // what the example should look like if they adhered to the spec (i.e., 0 instants should be empty strings)
        AdSchedule ads = TypeConvert.jsonToObject(
            "{\"snooze_count\":3,\"snooze_refresh_at\":\"\",\"next_ad_at\":\"\",\"length_seconds\":0,\"last_ad_at\":\"\",\"preroll_free_time_seconds\":0}",
            AdSchedule.class
        );
        assertNotNull(ads);
        assertEquals(3, ads.getSnoozeCount());
        assertEquals(0, ads.getLengthSeconds());
        assertEquals(0, ads.getPrerollFreeTimeSeconds());
        assertNull(ads.getSnoozeRefreshAt());
        assertNull(ads.getNextAdAt());
        assertNull(ads.getLastAdAt());
    }

    @Test
    void deserializeSample() {
        // the example response from the official docs
        AdSchedule ads = TypeConvert.jsonToObject(
            "{\"next_ad_at\":\"2023-08-01T23:08:18+00:00\",\"last_ad_at\":\"2023-08-01T23:08:18+00:00\",\"length_seconds\":\"60\",\"preroll_free_time_seconds\":\"90\",\"snooze_count\":\"1\",\"snooze_refresh_at\":\"2023-08-01T23:08:18+00:00\"}",
            AdSchedule.class
        );
        assertNotNull(ads);
        assertEquals(1, ads.getSnoozeCount());
        assertEquals(60, ads.getLengthSeconds());
        assertEquals(90, ads.getPrerollFreeTimeSeconds());
        assertEquals(Instant.parse("2023-08-01T23:08:18Z"), ads.getNextAdAt());
        assertEquals(Instant.parse("2023-08-01T23:08:18Z"), ads.getLastAdAt());
        assertEquals(Instant.parse("2023-08-01T23:08:18Z"), ads.getSnoozeRefreshAt());
    }

    @Test
    void deserializeSampleSpec() {
        // the example response from the official docs, if it followed the spec (i.e., integers are not strings)
        AdSchedule ads = TypeConvert.jsonToObject(
            "{\"next_ad_at\":\"2023-08-01T23:08:18+00:00\",\"last_ad_at\":\"2023-08-01T23:08:18+00:00\",\"length_seconds\":60,\"preroll_free_time_seconds\":90,\"snooze_count\":1,\"snooze_refresh_at\":\"2023-08-01T23:08:18+00:00\"}",
            AdSchedule.class
        );
        assertNotNull(ads);
        assertEquals(1, ads.getSnoozeCount());
        assertEquals(60, ads.getLengthSeconds());
        assertEquals(90, ads.getPrerollFreeTimeSeconds());
        assertEquals(Instant.parse("2023-08-01T23:08:18Z"), ads.getNextAdAt());
        assertEquals(Instant.parse("2023-08-01T23:08:18Z"), ads.getLastAdAt());
        assertEquals(Instant.parse("2023-08-01T23:08:18Z"), ads.getSnoozeRefreshAt());
    }

}
