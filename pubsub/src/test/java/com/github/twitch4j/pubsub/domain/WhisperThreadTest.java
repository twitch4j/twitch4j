package com.github.twitch4j.pubsub.domain;

import com.github.twitch4j.common.util.TypeConvert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unittest")
class WhisperThreadTest {

    @Test
    void deserialize() {
        String json = "{\"id\":\"53888434_268669435\",\"last_read\":13006,\"archived\":false,\"muted\":false," +
            "\"spam_info\":{\"likelihood\":\"low\",\"last_marked_not_spam\":0}," +
            "\"whitelisted_until\":\"2023-06-09T06:51:19Z\"}";
        WhisperThread data = TypeConvert.jsonToObject(json, WhisperThread.class);
        assertNotNull(data);
        assertEquals(13006, data.getLastRead());
        assertFalse(data.getArchived());
        assertFalse(data.getMuted());
        assertNotNull(data.getSpamInfo());
        assertEquals("low", data.getSpamInfo().getLikelihood());
        assertEquals(Instant.parse("2023-06-09T06:51:19Z"), data.getAllowlistedUntil());
        assertEquals("268669435", data.getOtherUserId());
    }

}
