package com.github.twitch4j.pubsub.domain;

import com.github.twitch4j.common.util.TypeConvert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Tag("unittest")
class HypeTrainApproachingTest {

    @Test
    void deserialize() {
        HypeTrainApproaching data = TypeConvert.jsonToObject(
            "{\"channel_id\":\"173164131\",\"goal\":3,\"events_remaining_durations\":{\"1\":221},\"level_one_rewards\":[{\"type\":\"EMOTE\",\"id\":\"emotesv2_bc2ca1d0a58b4731a9fc3432cb175c86\",\"group_id\":\"\",\"reward_level\":0,\"set_id\":\"8a3d6e77-b5cd-48dc-84f8-ad880db54e45\",\"token\":\"BatterUp\",\"reward_end_date\":\"0001-01-01T00:00:00Z\"},{\"type\":\"EMOTE\",\"id\":\"emotesv2_692f743d3e7147068bb1ddf842f9b99d\",\"group_id\":\"\",\"reward_level\":0,\"set_id\":\"8a3d6e77-b5cd-48dc-84f8-ad880db54e45\",\"token\":\"GoodOne\",\"reward_end_date\":\"0001-01-01T00:00:00Z\"},{\"type\":\"EMOTE\",\"id\":\"emotesv2_aa8db3de21e1465dab81bedfa47e29f2\",\"group_id\":\"\",\"reward_level\":0,\"set_id\":\"8a3d6e77-b5cd-48dc-84f8-ad880db54e45\",\"token\":\"MegaConsume\",\"reward_end_date\":\"0001-01-01T00:00:00Z\"},{\"type\":\"EMOTE\",\"id\":\"emotesv2_a3cdcbfcae9b41bb8215b012362eea35\",\"group_id\":\"\",\"reward_level\":0,\"set_id\":\"8a3d6e77-b5cd-48dc-84f8-ad880db54e45\",\"token\":\"FrogPonder\",\"reward_end_date\":\"0001-01-01T00:00:00Z\"},{\"type\":\"EMOTE\",\"id\":\"emotesv2_7fa0ba50748c418d956afa59c2e94883\",\"group_id\":\"\",\"reward_level\":0,\"set_id\":\"8a3d6e77-b5cd-48dc-84f8-ad880db54e45\",\"token\":\"ChillGirl\",\"reward_end_date\":\"0001-01-01T00:00:00Z\"},{\"type\":\"EMOTE\",\"id\":\"emotesv2_92d34a3642744c6bb540b091d3e9e9b0\",\"group_id\":\"\",\"reward_level\":0,\"set_id\":\"8a3d6e77-b5cd-48dc-84f8-ad880db54e45\",\"token\":\"ButtonMash\",\"reward_end_date\":\"0001-01-01T00:00:00Z\"}],\"creator_color\":\"\",\"participants\":[\"109707222\",\"100371732\"],\"approaching_hype_train_id\":\"d5562d1b-41a6-49dd-a8dd-1c10ac8ba3b7\",\"is_boost_train\":false,\"is_golden_kappa_train\":false,\"expires_at\":\"2024-06-28T18:18:23.522181847Z\"}",
            HypeTrainApproaching.class
        );

        assertEquals("173164131", data.getChannelId());
        assertEquals(3, data.getGoal());
        assertEquals(Collections.singletonMap("1", 221L), data.getEventsRemainingDurations());
        assertEquals("", data.getCreatorColor());
        assertEquals("d5562d1b-41a6-49dd-a8dd-1c10ac8ba3b7", data.getApproachingHypeTrainId());
        assertFalse(data.isBoostTrain());
        assertFalse(data.isGoldenKappaTrain());
        assertEquals(Instant.parse("2024-06-28T18:18:23.522181847Z"), data.getExpiresAt());
        assertEquals(Arrays.asList("109707222", "100371732"), data.getParticipantUserIds());
        HypeTrainReward reward = data.getLevelOneRewards().get(0);
        assertEquals("EMOTE", reward.getType());
        assertEquals("emotesv2_bc2ca1d0a58b4731a9fc3432cb175c86", reward.getId());
        assertEquals("", reward.getGroupId());
        assertEquals(0, reward.getRewardLevel());
        assertEquals("8a3d6e77-b5cd-48dc-84f8-ad880db54e45", reward.getSetId());
        assertEquals("BatterUp", reward.getToken());
        assertFalse(reward.isTemporary());
    }

}
