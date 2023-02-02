package com.github.twitch4j.pubsub.domain;

import com.github.twitch4j.common.util.TypeConvert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unittest")
class CreateShoutoutDataTest {

    @Test
    @DisplayName("Deserialize Create Shoutout PubSub Data without CTA")
    void deserializeSimple() {
        String json = "{\"broadcasterUserID\":\"135074820\",\"targetUserID\":\"42941889\",\"targetLogin\":\"arnaudryku\"," +
            "\"targetUserProfileImageURL\":\"https://static-cdn.jtvnw.net/jtv_user_pictures/71cc5b31-6cc1-4761-b7c4-7a1fccfaa56a-profile_image-%s.jpeg\"," +
            "\"sourceUserID\":\"72738644\",\"sourceLogin\":\"diatorker\",\"shoutoutID\":\"d2a6ac54-1d8a-4e44-8b5d-f7c1ee4938e6\"," +
            "\"targetUserDisplayName\":\"ArnaudRyku\",\"targetUserCTAInfo\":\"\",\"targetUserPrimaryColorHex\":\"000000\"}";

        CreateShoutoutData data = assertDoesNotThrow(() -> TypeConvert.jsonToObject(json, CreateShoutoutData.class));
        assertEquals("d2a6ac54-1d8a-4e44-8b5d-f7c1ee4938e6", data.getShoutoutId());
        assertEquals("135074820", data.getBroadcasterId());
        assertEquals("72738644", data.getModeratorId());
        assertEquals("diatorker", data.getModeratorLogin());
        assertEquals("42941889", data.getTargetId());
        assertEquals("arnaudryku", data.getTargetLogin());
        assertEquals("ArnaudRyku", data.getTargetDisplayName());
        assertEquals("000000", data.getTargetUserColorHex());
    }

}
