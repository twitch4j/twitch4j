package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.common.enums.CommandPermission;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unittest")
public class IRCMessageEventTest {

    @Test
    @DisplayName("Tests that CLEARCHAT is parsed by IRCMessageEvent")
    void parseChatClear() {
        IRCMessageEvent e = build("@room-id=12345678;tmi-sent-ts=1642715756806 :tmi.twitch.tv CLEARCHAT #dallas");

        assertEquals("CLEARCHAT", e.getCommandType());
        assertEquals("dallas", e.getChannelName().orElse(null));
        assertEquals("12345678", e.getChannelId());
    }

    @Test
    @DisplayName("Tests that CLEARMSG is parsed by IRCMessageEvent")
    void parseMessageDeletion() {
        IRCMessageEvent e = build("@login=foo;room-id=;target-msg-id=94e6c7ff-bf98-4faa-af5d-7ad633a158a9;tmi-sent-ts=1642720582342 :tmi.twitch.tv CLEARMSG #bar :what a great day");

        assertEquals("foo", e.getUserName());
        assertEquals("bar", e.getChannelName().orElse(null));
        assertEquals("CLEARMSG", e.getCommandType());
        assertEquals("what a great day", e.getMessage().orElse(null));
        assertEquals("94e6c7ff-bf98-4faa-af5d-7ad633a158a9", e.getTagValue("target-msg-id").orElse(null));
    }

    @Test
    @DisplayName("Test that normal messages are parsed by IRCMessageEvent")
    void parseMessage() {
        IRCMessageEvent e = build("@badge-info=;badges=broadcaster/1;client-nonce=459e3142897c7a22b7d275178f2259e0;color=#0000FF;display-name=lovingt3s;emote-only=1;emotes=62835:0-10;first-msg=0;flags=;" +
            "id=885196de-cb67-427a-baa8-82f9b0fcd05f;mod=0;room-id=713936733;subscriber=0;tmi-sent-ts=1643904084794;turbo=0;user-id=713936733;user-type= " +
            ":lovingt3s!lovingt3s@lovingt3s.tmi.twitch.tv PRIVMSG #lovingt3s :bleedPurple");

        assertEquals("bleedPurple", e.getMessage().orElse(null));
        assertEquals("lovingt3s", e.getChannelName().orElse(null));
        assertEquals("713936733", e.getChannelId());
        assertEquals("713936733", e.getUserId());
        assertEquals("lovingt3s", e.getUserName());
        assertEquals("PRIVMSG", e.getCommandType());
        assertTrue(e.getClientPermissions().contains(CommandPermission.BROADCASTER));
        assertEquals("885196de-cb67-427a-baa8-82f9b0fcd05f", e.getMessageId().orElse(null));
        assertEquals("459e3142897c7a22b7d275178f2259e0", e.getNonce().orElse(null));
        assertEquals("62835:0-10", e.getTagValue("emotes").orElse(null));
    }

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
