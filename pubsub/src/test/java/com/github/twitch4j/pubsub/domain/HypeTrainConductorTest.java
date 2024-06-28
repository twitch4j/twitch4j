package com.github.twitch4j.pubsub.domain;

import com.github.twitch4j.common.util.TypeConvert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unittest")
class HypeTrainConductorTest {

    @Test
    void deserialize() {
        HypeTrainConductor data = TypeConvert.jsonToObject(
            "{\"id\":\"d5562d1b-41a6-49dd-a8dd-1c10ac8ba3b7-BITS\",\"source\":\"BITS\",\"user\":{\"id\":\"71326631\",\"login\":\"a7xrixstar\",\"display_name\":\"a7xrixstar\",\"profile_image_url\":\"https://static-cdn.jtvnw.net/user-default-pictures-uv/ebe4cd89-b4f4-4cd9-adac-2f30151b4209-profile_image-50x50.png\"},\"participations\":{\"BITS.CHEER\":1000}}",
            HypeTrainConductor.class
        );
        assertEquals("d5562d1b-41a6-49dd-a8dd-1c10ac8ba3b7-BITS", data.getId());
        assertEquals("BITS", data.getSource());
        assertEquals("71326631", data.getUser().getId());
        assertEquals("a7xrixstar", data.getUser().getLogin());
        assertEquals(1000, data.getParticipations().getCheerBits());
    }

}
