package com.github.twitch4j.pubsub.domain;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.enums.PubSubType;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unittest")
class RevocationTest {

    @Test
    void parseAsResponse() {
        String json = "{\"type\":\"AUTH_REVOKED\",\"data\":{\"topics\":[\"channel-bits-events-v1.44322889\"]}}";
        PubSubResponse message = TypeConvert.jsonToObject(json, PubSubResponse.class);
        assertNotNull(message);
        assertEquals(PubSubType.AUTH_REVOKED, message.getType());
    }

    @Test
    void parseAsRequest() {
        String json = "{\"type\":\"AUTH_REVOKED\",\"data\":{\"topics\":[\"channel-bits-events-v1.44322889\"]}}";
        PubSubRequest message = TypeConvert.jsonToObject(json, PubSubRequest.class);
        assertNotNull(message);
        assertEquals(PubSubType.AUTH_REVOKED, message.getType());
        assertEquals(Collections.singletonList("channel-bits-events-v1.44322889"), message.getData().get("topics"));
    }

}
