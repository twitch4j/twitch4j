package com.github.twitch4j.chat.events.channel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unittest")
public class IRCMessageEventTest {

    @Test
    @DisplayName("Test that whispers are parsed by IRCMessageEvent")
    void parseWhisper() {
        IRCMessageEvent e = build("@badges=;color=;display-name=HexaFice;emotes=;message-id=103;thread-id=142621956_149223493;turbo=0;user-id=142621956;user-type= " +
            ":hexafice!hexafice@hexafice.tmi.twitch.tv WHISPER twitch4j :test 123");

        assertEquals("test 123", e.getMessage().orElse(null));
        assertEquals("WHISPER", e.getCommandType());
        assertEquals("142621956", e.getUserId());
        assertEquals("hexafice", e.getUserName());
        assertEquals("HexaFice", e.getTagValue("display-name").orElse(null));
        assertEquals("twitch4j", e.getChannelName().orElse(null));
        assertTrue(e.getBadges() == null || e.getBadges().isEmpty());
        assertTrue(e.getBadgeInfo() == null || e.getBadgeInfo().isEmpty());
    }

    private static IRCMessageEvent build(String raw) {
        return new IRCMessageEvent(raw, Collections.emptyMap(), Collections.emptyMap(), Collections.emptySet());
    }

}
