package com.github.twitch4j.pubsub.domain;

import com.github.twitch4j.common.util.TypeConvert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Tag("unittest")
class HypeProgressionTest {

    @Test
    void deserialize() {
        HypeProgression data = TypeConvert.jsonToObject(
            "{\"id\":\"d5562d1b-41a6-49dd-a8dd-1c10ac8ba3b7\",\"user_id\":\"71326631\",\"user_login\":\"a7xrixstar\",\"user_display_name\":\"a7xrixstar\",\"user_profile_image_url\":\"https://static-cdn.jtvnw.net/user-default-pictures-uv/ebe4cd89-b4f4-4cd9-adac-2f30151b4209-profile_image-50x50.png\",\"sequence_id\":4500,\"action\":\"CHEER\",\"source\":\"BITS\",\"quantity\":1000,\"progress\":{\"level\":{\"value\":3,\"goal\":7600,\"rewards\":[{\"type\":\"EMOTE\",\"id\":\"emotesv2_2a52b54c6fb04a6fbb6b9eb51fa8e0d0\",\"group_id\":\"\",\"reward_level\":0,\"set_id\":\"db68b384-8eae-470e-9cde-2945ac64fd7d\",\"token\":\"MegaMlep\",\"reward_end_date\":\"0001-01-01T00:00:00Z\"},{\"type\":\"EMOTE\",\"id\":\"emotesv2_91b3e913c6484fca894830ab953aa16b\",\"group_id\":\"\",\"reward_level\":0,\"set_id\":\"db68b384-8eae-470e-9cde-2945ac64fd7d\",\"token\":\"RawkOut\",\"reward_end_date\":\"0001-01-01T00:00:00Z\"},{\"type\":\"EMOTE\",\"id\":\"emotesv2_8120b15b9e054b31a200a5cb6cade4c7\",\"group_id\":\"\",\"reward_level\":0,\"set_id\":\"db68b384-8eae-470e-9cde-2945ac64fd7d\",\"token\":\"FallDamage\",\"reward_end_date\":\"0001-01-01T00:00:00Z\"},{\"type\":\"EMOTE\",\"id\":\"emotesv2_a83f8ade02cd4b37b8ae079584407c66\",\"group_id\":\"\",\"reward_level\":0,\"set_id\":\"db68b384-8eae-470e-9cde-2945ac64fd7d\",\"token\":\"RedCard\",\"reward_end_date\":\"0001-01-01T00:00:00Z\"},{\"type\":\"EMOTE\",\"id\":\"emotesv2_c3db311615df4ecb9e3be0c492fbfc8b\",\"group_id\":\"\",\"reward_level\":0,\"set_id\":\"db68b384-8eae-470e-9cde-2945ac64fd7d\",\"token\":\"ApplauseBreak\",\"reward_end_date\":\"0001-01-01T00:00:00Z\"},{\"type\":\"EMOTE\",\"id\":\"emotesv2_871fb6fa55d54fae8e807198c59e082f\",\"group_id\":\"\",\"reward_level\":0,\"set_id\":\"db68b384-8eae-470e-9cde-2945ac64fd7d\",\"token\":\"TouchOfSalt\",\"reward_end_date\":\"0001-01-01T00:00:00Z\"}]},\"value\":0,\"goal\":3100,\"total\":4500,\"remaining_seconds\":230,\"all_time_high_state\":\"NONE\"},\"is_boost_train\":false,\"initiator_currency\":null,\"is_fast_mode\":false,\"is_large_event\":false}",
            HypeProgression.class
        );

        assertEquals("d5562d1b-41a6-49dd-a8dd-1c10ac8ba3b7", data.getId());
        assertEquals("71326631", data.getUserId());
        assertEquals("a7xrixstar", data.getUserLogin());
        assertEquals(4500, data.getSequenceId());
        assertEquals("CHEER", data.getAction());
        assertEquals("BITS", data.getSource());
        assertEquals(1000, data.getQuantity());
        assertFalse(data.isBoostTrain());
        assertFalse(data.isFastMode());
        assertFalse(data.isLargeEvent());
        assertEquals(3100, data.getProgress().getGoal());
        assertEquals(4500, data.getProgress().getTotal());
        assertEquals(230, data.getProgress().getRemainingSeconds());
        assertEquals("NONE", data.getProgress().getAllTimeHighState());
        assertEquals(3, data.getProgress().getLevel().getValue());
        assertEquals(7600, data.getProgress().getLevel().getGoal());
        assertEquals("MegaMlep", data.getProgress().getLevel().getRewards().get(0).getToken());
    }

}
