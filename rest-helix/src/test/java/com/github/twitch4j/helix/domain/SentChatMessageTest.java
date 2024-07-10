package com.github.twitch4j.helix.domain;

import com.github.twitch4j.common.util.TypeConvert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unittest")
class SentChatMessageTest {

    @Test
    void deserializeSent() {
        String json = "{\"message_id\":\"e2a18344-83f9-4540-9513-422716fb079e\",\"is_sent\":true}";
        SentChatMessage data = TypeConvert.jsonToObject(json, SentChatMessage.class);
        assertEquals("e2a18344-83f9-4540-9513-422716fb079e", data.getMessageId());
        assertTrue(data.isSent());
    }

    @Test
    void deserializeDropped() {
        String json = "{\"drop_reason\":{\"code\":\"msg_rejected\",\"message\":\"Your message is being checked by mods and has not been sent.\"},\"is_sent\":false,\"message_id\":\"\"}";
        SentChatMessage data = TypeConvert.jsonToObject(json, SentChatMessage.class);
        assertTrue(data.getMessageId() == null || data.getMessageId().isEmpty());
        assertFalse(data.isSent());
        assertNotNull(data.getDropReason());
        assertEquals("msg_rejected", data.getDropReason().getCode());
        assertEquals("Your message is being checked by mods and has not been sent.", data.getDropReason().getMessage());
    }

}
