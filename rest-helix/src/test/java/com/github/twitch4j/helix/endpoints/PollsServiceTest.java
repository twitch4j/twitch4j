package com.github.twitch4j.helix.endpoints;

import com.github.twitch4j.eventsub.domain.PollChoice;
import com.github.twitch4j.eventsub.domain.PollStatus;
import com.github.twitch4j.helix.domain.Poll;
import com.github.twitch4j.helix.domain.PollsList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.github.twitch4j.common.util.TypeConvert.jsonToObject;
import static com.github.twitch4j.common.util.TypeConvert.objectToJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Tag("unittest")
public class PollsServiceTest extends AbstractEndpointTest {

    @Test
    @DisplayName("Deserialize PollsList from getPolls example")
    public void deserializePollsList() {
        // note: slightly modified from the sample in the official docs
        String json = "{\"data\":[{\"id\":\"ed961efd-8a3f-4cf5-a9d0-e616c590cd2a\",\"broadcaster_id\":\"55696719\",\"broadcaster_name\":\"TwitchDev\",\"broadcaster_login\":\"twitchdev\",\"title\":\"Heads or Tails?\"," +
            "\"choices\":[{\"id\":\"4c123012-1351-4f33-84b7-43856e7a0f47\",\"title\":\"Heads\",\"votes\":1,\"channel_points_votes\":69,\"bits_votes\":0},{\"id\":\"279087e3-54a7-467e-bcd0-c1393fcea4f0\",\"title\":\"Tails\",\"votes\":1," +
            "\"channel_points_votes\":0,\"bits_votes\":1}],\"bits_voting_enabled\":true,\"bits_per_vote\":0,\"channel_points_voting_enabled\":true,\"channel_points_per_vote\":69,\"status\":\"ACTIVE\",\"duration\":1800," +
            "\"started_at\":\"2021-03-19T06:08:33.871278372Z\"}],\"pagination\":{}}";

        PollsList polls = jsonToObject(json, PollsList.class);
        assertNotNull(polls);
        assertNotNull(polls.getPolls());
        assertEquals(1, polls.getPolls().size());

        Poll poll = polls.getPolls().get(0);
        assertNotNull(poll);
        assertEquals("ed961efd-8a3f-4cf5-a9d0-e616c590cd2a", poll.getId());
        assertEquals("55696719", poll.getBroadcasterId());
        assertEquals("TwitchDev", poll.getBroadcasterName());
        assertEquals("twitchdev", poll.getBroadcasterLogin());
        assertEquals("Heads or Tails?", poll.getTitle());
        assertTrue(poll.isBitsVotingEnabled());
        assertEquals(0, poll.getBitsPerVote());
        assertTrue(poll.isChannelPointsVotingEnabled());
        assertEquals(69, poll.getChannelPointsPerVote());
        assertEquals(PollStatus.ACTIVE, poll.getStatus());
        assertEquals(1800, poll.getDurationSeconds());
        assertEquals(Instant.parse("2021-03-19T06:08:33.871278372Z"), poll.getStartedAt());
        assertNull(poll.getEndedAt());
        assertNotNull(poll.getChoices());
        assertEquals(2, poll.getChoices().size());
        assertNotNull(poll.getChoices().get(0));
        assertEquals("4c123012-1351-4f33-84b7-43856e7a0f47", poll.getChoices().get(0).getId());
        assertEquals(69, poll.getChoices().get(0).getChannelPointsVotes());
        assertNotNull(poll.getChoices().get(1));
        assertEquals("279087e3-54a7-467e-bcd0-c1393fcea4f0", poll.getChoices().get(1).getId());
        assertEquals(1, poll.getChoices().get(1).getBitsVotes());
    }

    @Test
    @DisplayName("Serialize Poll as POJO body for createPoll")
    public void serializeCreatePoll() {
        Poll poll = Poll.builder()
            .broadcasterId("141981764")
            .title("Heads or Tails?")
            .isChannelPointsVotingEnabled(true)
            .channelPointsPerVote(100)
            .durationSeconds(1800)
            .choice(PollChoice.builder().title("Heads").build())
            .choice(PollChoice.builder().title("Tails").build())
            .build();

        assertEquals(poll, jsonToObject(objectToJson(poll), Poll.class));
        assertEquals(
            poll,
            jsonToObject("{\"broadcaster_id\":\"141981764\",\"title\":\"Heads or Tails?\",\"choices\":[{\"title\":\"Heads\"},{\"title\":\"Tails\"}],\"channel_points_voting_enabled\":true,\"channel_points_per_vote\":100,\"duration\":1800}", Poll.class)
        );
    }

    @Test
    @DisplayName("Serialize Poll as POJO body for endPoll")
    public void serializeEndPoll() {
        Poll poll = Poll.builder()
            .broadcasterId("141981764")
            .id("ed961efd-8a3f-4cf5-a9d0-e616c590cd2a")
            .status(PollStatus.TERMINATED)
            .build();

        assertEquals(poll, jsonToObject(objectToJson(poll), Poll.class));
        assertEquals(poll, jsonToObject("{\"broadcaster_id\":\"141981764\",\"id\":\"ed961efd-8a3f-4cf5-a9d0-e616c590cd2a\",\"status\":\"TERMINATED\"}", Poll.class));
    }

}
