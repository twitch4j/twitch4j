package com.github.twitch4j.pubsub.domain;

import com.github.twitch4j.common.util.TypeConvert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Tag("unittest")
class HypeTrainEndTest {

    @Test
    void deserialize() {
        HypeTrainEnd data = TypeConvert.jsonToObject(
            "{\"id\":\"72c6889f-4861-4480-bea9-bd399de03df3\",\"ended_at\":1719608817000,\"ending_reason\":\"COMPLETED\",\"is_boost_train\":false,\"participation_totals\":[{\"source\":\"SUBS\",\"action\":\"TIER_1_SUB\",\"quantity\":38},{\"source\":\"SUBS\",\"action\":\"TIER_2_SUB\",\"quantity\":1},{\"source\":\"BITS\",\"action\":\"CHEER\",\"quantity\":800}],\"rewards\":[{\"__typename\":\"HypeTrainEmoteReward\",\"id\":\"emotesv2_20a5c29af55240d4a276e0ffd828db3e\",\"type\":\"EMOTE\",\"emote\":{\"__typename\":\"Emote\",\"id\":\"emotesv2_20a5c29af55240d4a276e0ffd828db3e\",\"token\":\"PersonalBest\"}},{\"__typename\":\"HypeTrainEmoteReward\",\"id\":\"emotesv2_18479de9ad48456aab82a8c9e24e864b\",\"type\":\"EMOTE\",\"emote\":{\"__typename\":\"Emote\",\"id\":\"emotesv2_18479de9ad48456aab82a8c9e24e864b\",\"token\":\"HenloThere\"}},{\"__typename\":\"HypeTrainEmoteReward\",\"id\":\"emotesv2_0d9792a1c8d3499cac7c2b517dc0f682\",\"type\":\"EMOTE\",\"emote\":{\"__typename\":\"Emote\",\"id\":\"emotesv2_0d9792a1c8d3499cac7c2b517dc0f682\",\"token\":\"GimmeDat\"}},{\"__typename\":\"HypeTrainEmoteReward\",\"id\":\"emotesv2_e7c9f4491c9b44d68e41aff832851872\",\"type\":\"EMOTE\",\"emote\":{\"__typename\":\"Emote\",\"id\":\"emotesv2_e7c9f4491c9b44d68e41aff832851872\",\"token\":\"AGiftForYou\"}},{\"__typename\":\"HypeTrainEmoteReward\",\"id\":\"emotesv2_3969f334f5a2425d9fad53daabb06982\",\"type\":\"EMOTE\",\"emote\":{\"__typename\":\"Emote\",\"id\":\"emotesv2_3969f334f5a2425d9fad53daabb06982\",\"token\":\"KittyHype\"}},{\"__typename\":\"HypeTrainEmoteReward\",\"id\":\"emotesv2_da6ee66bc259434085eb866429687941\",\"type\":\"EMOTE\",\"emote\":{\"__typename\":\"Emote\",\"id\":\"emotesv2_da6ee66bc259434085eb866429687941\",\"token\":\"DangerDance\"}}]}",
            HypeTrainEnd.class
        );
        assertEquals("72c6889f-4861-4480-bea9-bd399de03df3", data.getId());
        assertEquals(Instant.ofEpochMilli(1719608817000L), data.getEndedAt());
        assertEquals("COMPLETED", data.getEndingReason());
        assertFalse(data.isBoostTrain());
        assertEquals(38, data.getParticipationTotals().get(0).getQuantity());
        assertEquals("emotesv2_20a5c29af55240d4a276e0ffd828db3e", data.getRewards().get(0).getId());
    }

}
