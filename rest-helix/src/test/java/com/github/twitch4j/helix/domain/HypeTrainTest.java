package com.github.twitch4j.helix.domain;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.eventsub.domain.Contribution;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unittest")
class HypeTrainTest {

    @Test
    void deserialize() {
        HypeTrainStatus data = TypeConvert.jsonToObject(
            "{\"current\":{\"id\":\"1b0AsbInCHZW2SQFQkCzqN07Ib2\",\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"cool_user\",\"broadcaster_user_name\":\"Cool_User\",\"level\":2,\"total\":700,\"progress\":200,\"goal\":1000,\"top_contributions\":[{\"user_id\":\"123\",\"user_login\":\"pogchamp\",\"user_name\":\"PogChamp\",\"type\":\"bits\",\"total\":50},{\"user_id\":\"456\",\"user_login\":\"kappa\",\"user_name\":\"Kappa\",\"type\":\"subscription\",\"total\":45}],\"shared_train_participants\":[{\"broadcaster_user_id\":\"456\",\"broadcaster_user_login\":\"pogchamp\",\"broadcaster_user_name\":\"PogChamp\"},{\"broadcaster_user_id\":\"321\",\"broadcaster_user_login\":\"pogchamp\",\"broadcaster_user_name\":\"PogChamp\"}],\"started_at\":\"2020-07-15T17:16:03.17106713Z\",\"expires_at\":\"2020-07-15T17:16:11.17106713Z\",\"type\":\"golden_kappa\",\"is_shared_train\":true},\"all_time_high\":{\"level\":6,\"total\":2850,\"achieved_at\":\"2020-04-24T20:12:21.003802269Z\"},\"shared_all_time_high\":{\"level\":16,\"total\":23850,\"achieved_at\":\"2020-04-27T20:12:21.003802269Z\"}}",
            HypeTrainStatus.class
        );

        HypeTrain train = data.getCurrent();
        assertNotNull(train);
        assertEquals("1b0AsbInCHZW2SQFQkCzqN07Ib2", train.getId());
        assertEquals("1337", train.getBroadcasterUserId());
        assertEquals("cool_user", train.getBroadcasterUserLogin());
        assertEquals("Cool_User", train.getBroadcasterUserName());
        assertEquals(2, train.getLevel());
        assertEquals(700, train.getTotal());
        assertEquals(200, train.getProgress());
        assertEquals(1000, train.getGoal());
        assertEquals(Instant.parse("2020-07-15T17:16:03.17106713Z"), train.getStartedAt());
        assertEquals(Instant.parse("2020-07-15T17:16:11.17106713Z"), train.getExpiresAt());
        assertEquals(HypeTrain.Type.GOLDEN_KAPPA, train.getType());
        assertTrue(train.isSharedTrain());
        assertNotNull(train.getTopContributions());
        assertEquals(2, train.getTopContributions().size());
        assertEquals("123", train.getTopContributions().get(0).getUserId());
        assertEquals("pogchamp", train.getTopContributions().get(0).getUserLogin());
        assertEquals(Contribution.Type.BITS, train.getTopContributions().get(0).getType());
        assertEquals(50, train.getTopContributions().get(0).getTotal());
        assertEquals(Contribution.Type.SUBSCRIPTION, train.getTopContributions().get(1).getType());
        assertEquals(45, train.getTopContributions().get(1).getTotal());
        assertNotNull(train.getSharedTrainParticipants());
        assertEquals(2, train.getSharedTrainParticipants().size());
        assertEquals("456", train.getSharedTrainParticipants().get(0).getBroadcasterId());

        assertNotNull(data.getAllTimeHigh());
        assertEquals(6, data.getAllTimeHigh().getLevel());
        assertEquals(2850, data.getAllTimeHigh().getTotal());
        assertEquals(Instant.parse("2020-04-24T20:12:21.003802269Z"), data.getAllTimeHigh().getAchievedAt());

        assertNotNull(data.getSharedAllTimeHigh());
        assertEquals(16, data.getSharedAllTimeHigh().getLevel());
        assertEquals(23850, data.getSharedAllTimeHigh().getTotal());
        assertEquals(Instant.parse("2020-04-27T20:12:21.003802269Z"), data.getSharedAllTimeHigh().getAchievedAt());
    }

}
