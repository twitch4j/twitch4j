package com.github.twitch4j.internal;

import com.google.code.regexp.Matcher;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Tag("unittest")
class ChatCommandHelixForwarderTest {

    @Test
    @DisplayName("Test that ChatCommandHelixForwarder#COMMAND_PATTERN matches accurately")
    void testCommandPattern() {
        Matcher matcher = ChatCommandHelixForwarder.COMMAND_PATTERN
            .matcher("@reply-parent-msg-id=uuid-1234 PRIVMSG #ancomlibra :/commercial");

        assertTrue(matcher.matches());
        assertEquals("reply-parent-msg-id=uuid-1234", matcher.group("tags"));
        assertEquals("ancomlibra", matcher.group("channel"));
        assertEquals("commercial", matcher.group("command"));

        matcher.reset("PRIVMSG #jmarianne :hello comrades");
        assertFalse(matcher.matches());

        matcher.reset("PRIVMSG #livagar : /marker poggers");
        assertTrue(matcher.matches());
        assertEquals("livagar", matcher.group("channel"));
        assertEquals("marker poggers", matcher.group("command"));

        matcher.reset("PRIVMSG #keffals :.ban null");
        assertTrue(matcher.matches());
        assertEquals("keffals", matcher.group("channel"));
        assertEquals("ban null", matcher.group("command"));
    }

}
