package com.github.twitch4j.eventsub.events;

import com.github.twitch4j.common.enums.AnnouncementColor;
import com.github.twitch4j.common.enums.SubscriptionPlan;
import com.github.twitch4j.eventsub.domain.AutomodCategory;
import com.github.twitch4j.eventsub.domain.AutomodMessageStatus;
import com.github.twitch4j.eventsub.domain.ContentClassification;
import com.github.twitch4j.eventsub.domain.Contribution;
import com.github.twitch4j.eventsub.domain.PollStatus;
import com.github.twitch4j.eventsub.domain.PredictionColor;
import com.github.twitch4j.eventsub.domain.PredictionOutcome;
import com.github.twitch4j.eventsub.domain.PredictionStatus;
import com.github.twitch4j.eventsub.domain.RedemptionStatus;
import com.github.twitch4j.eventsub.domain.StreamType;
import com.github.twitch4j.eventsub.domain.chat.Announcement;
import com.github.twitch4j.eventsub.domain.chat.Fragment;
import com.github.twitch4j.eventsub.domain.chat.Message;
import com.github.twitch4j.eventsub.domain.chat.NoticeType;
import com.github.twitch4j.eventsub.domain.chat.Resubscription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static com.github.twitch4j.common.util.TypeConvert.jsonToObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unittest")
public class EventSubEventTest {

    @Test
    @DisplayName("Deserialize ChannelAdBreakBeginEvent")
    public void deserializeAdBreakEvent() {
        ChannelAdBreakBeginEvent event = jsonToObject(
            "{\"length_seconds\":\"60\",\"started_at\":\"2019-11-16T10:11:12.634234626Z\",\"is_automatic\":\"false\",\"broadcaster_user_id\":\"1337\",\"requester_user_id\":\"1337\",\"broadcaster_user_login\":\"cool_user\",\"broadcaster_user_name\":\"Cool_User\"}",
            ChannelAdBreakBeginEvent.class
        );
        assertFalse(event.isAutomatic());
        assertEquals(60, event.getLengthSeconds());
        assertEquals("1337", event.getBroadcasterUserId());
        assertEquals("1337", event.getRequesterUserId());
        assertEquals(Instant.parse("2019-11-16T10:11:12.634234626Z"), event.getStartedAt());
    }

    @Test
    @DisplayName("Deserialize ShieldModeBeginEvent")
    public void deserializeModeratorEvent() {
        ShieldModeBeginEvent event = jsonToObject(
            "{\"broadcaster_user_id\":\"12345\",\"broadcaster_user_name\":\"SimplySimple\",\"broadcaster_user_login\":\"simplysimple\",\"moderator_user_id\":\"98765\",\"moderator_user_name\":\"ParticularlyParticular123\",\"moderator_user_login\":\"particularlyparticular123\",\"started_at\":\"2022-07-26T17:00:03.17106713Z\"}",
            ShieldModeBeginEvent.class
        );
        assertEquals("12345", event.getBroadcasterUserId());
        assertEquals("SimplySimple", event.getBroadcasterUserName());
        assertEquals("98765", event.getModeratorUserId());
        assertEquals("ParticularlyParticular123", event.getModeratorUserName());
        assertEquals(Instant.parse("2022-07-26T17:00:03.17106713Z"), event.getStartedAt());
    }

    @Test
    @DisplayName("Deserialize ChannelBanEvent: Timeout")
    public void deserializeModerationEvent() {
        ChannelBanEvent event = jsonToObject(
            "{\"user_id\":\"1234\",\"user_login\":\"cool_user\",\"user_name\":\"Cool_User\",\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"cooler_user\",\"broadcaster_user_name\":\"Cooler_User\",\"moderator_user_id\":\"1339\"," +
                "\"moderator_user_login\":\"mod_user\",\"moderator_user_name\":\"Mod_User\",\"reason\":\"Offensive language\",\"banned_at\":\"2020-07-15T18:15:11.17106713Z\",\"ends_at\":\"2020-07-15T18:16:11.17106713Z\",\"is_permanent\":false}",
            ChannelBanEvent.class
        );

        assertEquals("1234", event.getUserId());
        assertEquals("cool_user", event.getUserLogin());
        assertEquals("Cool_User", event.getUserName());

        assertEquals("1337", event.getBroadcasterUserId());
        assertEquals("cooler_user", event.getBroadcasterUserLogin());
        assertEquals("Cooler_User", event.getBroadcasterUserName());

        assertEquals("1339", event.getModeratorUserId());
        assertEquals("mod_user", event.getModeratorUserLogin());
        assertEquals("Mod_User", event.getModeratorUserName());

        assertEquals("Offensive language", event.getReason());
        assertEquals(Instant.parse("2020-07-15T18:15:11.17106713Z"), event.getBannedAt());
        assertEquals(Instant.parse("2020-07-15T18:16:11.17106713Z"), event.getEndsAt());
        assertFalse(event.isPermanent());
    }

    @Test
    @DisplayName("Deserialize ChannelBanEvent: Ban")
    public void deserializeModerationEvent2() {
        ChannelBanEvent event = jsonToObject(
            "{\"user_id\":\"1234\",\"user_login\":\"cool_user\",\"user_name\":\"Cool_User\",\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"cooler_user\",\"broadcaster_user_name\":\"Cooler_User\",\"moderator_user_id\":\"1339\"," +
                "\"moderator_user_login\":\"mod_user\",\"moderator_user_name\":\"Mod_User\",\"reason\":\"Offensive language\",\"ends_at\":null,\"is_permanent\":true}",
            ChannelBanEvent.class
        );

        assertTrue(event.isPermanent());
        assertNull(event.getEndsAt());
    }

    @Test
    @DisplayName("Deserialize ChannelPollBeginEvent")
    public void deserializePollBeginEvent() {
        ChannelPollBeginEvent event = jsonToObject(
            "{\"id\":\"1243456\",\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"cool_user\",\"broadcaster_user_name\":\"Cool_User\",\"title\":\"Aren’t shoes just really hard socks?\",\"choices\":[{\"id\":\"123\",\"title\":\"Yeah!\"}," +
                "{\"id\":\"124\",\"title\":\"No!\"},{\"id\":\"125\",\"title\":\"Maybe!\"}],\"bits_voting\":{\"is_enabled\":true,\"amount_per_vote\":10},\"channel_points_voting\":{\"is_enabled\":true,\"amount_per_vote\":10}," +
                "\"started_at\":\"2020-07-15T17:16:03.17106713Z\",\"ends_at\":\"2020-07-15T17:16:08.17106713Z\"}",
            ChannelPollBeginEvent.class
        );

        assertNotNull(event);
        assertEquals("1243456", event.getPollId());
        assertEquals("Aren’t shoes just really hard socks?", event.getTitle());
        assertNotNull(event.getChoices());
        assertEquals(3, event.getChoices().size());
        assertEquals("123", event.getChoices().get(0).getId());
        assertEquals("Maybe!", event.getChoices().get(2).getTitle());
        assertNotNull(event.getChannelPointsVoting());
        assertEquals(10, event.getChannelPointsVoting().getAmountPerVote());
        assertEquals(Instant.parse("2020-07-15T17:16:03.17106713Z"), event.getStartedAt());
        assertEquals(Instant.parse("2020-07-15T17:16:08.17106713Z"), event.getEndsAt());
    }

    @Test
    @DisplayName("Deserialize ChannelPollProgressEvent")
    public void deserializePollProgressEvent() {
        ChannelPollProgressEvent event = jsonToObject(
            "{\"id\":\"1243456\",\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"cool_user\",\"broadcaster_user_name\":\"Cool_User\",\"title\":\"Aren’t shoes just really hard socks?\",\"choices\":[{\"id\":\"123\",\"title\":\"Yeah!\"," +
                "\"bits_votes\":5,\"channel_points_votes\":7,\"votes\":12},{\"id\":\"124\",\"title\":\"No!\",\"bits_votes\":10,\"channel_points_votes\":4,\"votes\":14},{\"id\":\"125\",\"title\":\"Maybe!\",\"bits_votes\":0,\"channel_points_votes\":7," +
                "\"votes\":7}],\"bits_voting\":{\"is_enabled\":true,\"amount_per_vote\":10},\"channel_points_voting\":{\"is_enabled\":true,\"amount_per_vote\":10},\"started_at\":\"2020-07-15T17:16:03.17106713Z\",\"ends_at\":\"2020-07-15T17:16:08" +
                ".17106713Z\"}",
            ChannelPollProgressEvent.class
        );

        assertNotNull(event);
        assertNotNull(event.getChoices());
        assertEquals(3, event.getChoices().size());
        assertNotNull(event.getChoices().get(1));
        assertEquals(4, event.getChoices().get(1).getChannelPointsVotes());
        assertNotNull(event.getChoices().get(2));
        assertEquals(7, event.getChoices().get(2).getVotes());
    }

    @Test
    @DisplayName("Deserialize ChannelPollEndEvent")
    public void deserializePollEndEvent() {
        ChannelPollEndEvent event = jsonToObject(
            "{\"id\":\"1243456\",\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"cool_user\",\"broadcaster_user_name\":\"Cool_User\",\"title\":\"Aren’t shoes just really hard socks?\",\"choices\":[{\"id\":\"123\",\"title\":\"Blue\"," +
                "\"bits_votes\":50,\"channel_points_votes\":70,\"votes\":120},{\"id\":\"124\",\"title\":\"Yellow\",\"bits_votes\":100,\"channel_points_votes\":40,\"votes\":140},{\"id\":\"125\",\"title\":\"Green\",\"bits_votes\":10," +
                "\"channel_points_votes\":70,\"votes\":80}],\"bits_voting\":{\"is_enabled\":true,\"amount_per_vote\":10},\"channel_points_voting\":{\"is_enabled\":true,\"amount_per_vote\":10},\"status\":\"completed\",\"started_at\":\"2020-07-15T17:16:03" +
                ".17106713Z\",\"ended_at\":\"2020-07-15T17:16:11.17106713Z\"}",
            ChannelPollEndEvent.class
        );

        assertNotNull(event);
        assertEquals(PollStatus.COMPLETED, event.getStatus());
        assertNotNull(event.getEndedAt());
    }

    @Test
    @DisplayName("Deserialize ChannelPredictionBeginEvent")
    public void deserializePredictionBeginEvent() {
        ChannelPredictionBeginEvent event = jsonToObject(
            "{\"id\":\"1243456\",\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"cool_user\",\"broadcaster_user_name\":\"Cool_User\",\"title\":\"Aren’t shoes just really hard socks?\",\"outcomes\":[{\"id\":\"1243456\",\"title\":\"Yeah!\"," +
                "\"color\":\"blue\"},{\"id\":\"2243456\",\"title\":\"No!\",\"color\":\"pink\"}],\"started_at\":\"2020-07-15T17:16:03.17106713Z\",\"locks_at\":\"2020-07-15T17:21:03.17106713Z\"}",
            ChannelPredictionBeginEvent.class
        );

        assertNotNull(event);
        assertEquals("1243456", event.getPredictionId());
        assertEquals("Aren’t shoes just really hard socks?", event.getTitle());
        assertNotNull(event.getOutcomes());
        assertEquals(2, event.getOutcomes().size());
        assertNotNull(event.getOutcomes().get(0));
        assertEquals("1243456", event.getOutcomes().get(0).getId());
        assertEquals("Yeah!", event.getOutcomes().get(0).getTitle());
        assertEquals(PredictionColor.BLUE, event.getOutcomes().get(0).getColor());
        assertNotNull(event.getOutcomes().get(1));
        assertEquals(PredictionColor.PINK, event.getOutcomes().get(1).getColor());
        assertEquals(Instant.parse("2020-07-15T17:16:03.17106713Z"), event.getStartedAt());
        assertEquals(Instant.parse("2020-07-15T17:21:03.17106713Z"), event.getLocksAt());
    }

    @Test
    @DisplayName("Deserialize ChannelPredictionProgressEvent")
    public void deserializePredictionProgressEvent() {
        ChannelPredictionProgressEvent event = jsonToObject(
            "{\"id\":\"1243456\",\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"cool_user\",\"broadcaster_user_name\":\"Cool_User\",\"title\":\"Aren’t shoes just really hard socks?\",\"outcomes\":[{\"id\":\"1243456\",\"title\":\"Yeah!\"," +
                "\"color\":\"blue\",\"users\":10,\"channel_points\":15000,\"top_predictors\":[{\"user_name\":\"Cool_User\",\"user_login\":\"cool_user\",\"user_id\":1234,\"channel_points_won\":null,\"channel_points_used\":500}," +
                "{\"user_name\":\"Coolest_User\",\"user_login\":\"coolest_user\",\"user_id\":1236,\"channel_points_won\":null,\"channel_points_used\":200}]},{\"id\":\"2243456\",\"title\":\"No!\",\"color\":\"pink\"," +
                "\"top_predictors\":[{\"user_name\":\"Cooler_User\",\"user_login\":\"cooler_user\",\"user_id\":12345,\"channel_points_won\":null,\"channel_points_used\":5000}]}],\"started_at\":\"2020-07-15T17:16:03.17106713Z\"," +
                "\"locks_at\":\"2020-07-15T17:21:03.17106713Z\"}",
            ChannelPredictionProgressEvent.class
        );

        assertNotNull(event);
        assertNotNull(event.getOutcomes());
        assertEquals(2, event.getOutcomes().size());

        PredictionOutcome blue = event.getOutcomes().get(0);
        assertNotNull(blue);
        assertEquals(10, blue.getUsers());
        assertEquals(15000, blue.getChannelPoints());
        assertNotNull(blue.getTopPredictors());
        assertNotNull(blue.getTopPredictors().get(0));
        assertEquals("1234", blue.getTopPredictors().get(0).getUserId());
        assertNotNull(blue.getTopPredictors().get(1));
        assertEquals(200, blue.getTopPredictors().get(1).getChannelPointsUsed());

        PredictionOutcome pink = event.getOutcomes().get(1);
        assertNotNull(pink);
        assertNotNull(pink.getTopPredictors());
        assertEquals(1, pink.getTopPredictors().size());
        assertNotNull(pink.getTopPredictors().get(0));
        assertEquals("cooler_user", pink.getTopPredictors().get(0).getUserLogin());
        assertEquals("Cooler_User", pink.getTopPredictors().get(0).getUserName());
        assertNull(pink.getTopPredictors().get(0).getChannelPointsWon());

        assertNotNull(event.getLocksAt());
    }

    @Test
    @DisplayName("Deserialize ChannelPredictionEndEvent")
    @SuppressWarnings("ConstantConditions")
    public void deserializePredictionEndEvent() {
        ChannelPredictionEndEvent event = jsonToObject(
            "{\"id\":\"1243456\",\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"cool_user\",\"broadcaster_user_name\":\"Cool_User\",\"title\":\"Aren’t shoes just really hard socks?\",\"winning_outcome_id\":\"12345\"," +
                "\"outcomes\":[{\"id\":\"12345\",\"title\":\"Yeah!\",\"color\":\"blue\",\"users\":2,\"channel_points\":15000,\"top_predictors\":[{\"user_name\":\"Cool_User\",\"user_login\":\"cool_user\",\"user_id\":1234,\"channel_points_won\":10000," +
                "\"channel_points_used\":500},{\"user_name\":\"Coolest_User\",\"user_login\":\"coolest_user\",\"user_id\":1236,\"channel_points_won\":5000,\"channel_points_used\":100}]},{\"id\":\"22435\",\"title\":\"No!\",\"users\":2," +
                "\"channel_points\":200,\"color\":\"pink\",\"top_predictors\":[{\"user_name\":\"Cooler_User\",\"user_login\":\"cooler_user\",\"user_id\":12345,\"channel_points_won\":null,\"channel_points_used\":100},{\"user_name\":\"Elite_User\"," +
                "\"user_login\":\"elite_user\",\"user_id\":1337,\"channel_points_won\":null,\"channel_points_used\":100}]}],\"status\":\"resolved\",\"started_at\":\"2020-07-15T17:16:03.17106713Z\",\"ended_at\":\"2020-07-15T17:16:11.17106713Z\"}",
            ChannelPredictionEndEvent.class
        );

        assertNotNull(event);
        assertEquals("12345", event.getWinningOutcomeId());
        assertTrue(event.getWinningOutcome().isPresent());
        assertNotNull(event.getOutcomes());
        assertEquals(2, event.getOutcomes().size());
        assertNotNull(event.getOutcomes().get(0));
        assertNotNull(event.getOutcomes().get(0).getTopPredictors());
        assertEquals(2, event.getOutcomes().get(0).getTopPredictors().size());
        assertNotNull(event.getOutcomes().get(0).getTopPredictors().get(0));
        assertEquals(10000, event.getOutcomes().get(0).getTopPredictors().get(0).getChannelPointsWon());
        assertEquals(PredictionStatus.RESOLVED, event.getStatus());
        assertEquals(Instant.parse("2020-07-15T17:16:11.17106713Z"), event.getEndedAt());
    }

    @Test
    @DisplayName("Deserialize ChannelSharedChatUpdateEvent")
    public void deserializeSharedChatEvent() {
        ChannelSharedChatUpdateEvent event = jsonToObject(
            "{\"session_id\":\"2b64a92a-dbb8-424e-b1c3-304423ba1b6f\",\"broadcaster_user_id\":\"1971641\",\"broadcaster_user_login\":\"streamer\",\"broadcaster_user_name\":\"streamer\"," +
                "\"host_broadcaster_user_id\":\"1971641\",\"host_broadcaster_user_login\":\"streamer\",\"host_broadcaster_user_name\":\"streamer\"," +
                "\"participants\":[{\"broadcaster_user_id\":\"1971641\",\"broadcaster_user_name\":\"streamer\",\"broadcaster_user_login\":\"streamer\"},{\"broadcaster_user_id\":\"112233\",\"broadcaster_user_name\":\"streamer33\",\"broadcaster_user_login\":\"streamer33\"},{\"broadcaster_user_id\":\"332211\",\"broadcaster_user_name\":\"streamer11\",\"broadcaster_user_login\":\"streamer11\"}]}",
            ChannelSharedChatUpdateEvent.class
        );

        assertEquals("2b64a92a-dbb8-424e-b1c3-304423ba1b6f", event.getSessionId());
        assertEquals("1971641", event.getBroadcasterUserId());
        assertEquals("streamer", event.getHostBroadcasterUserLogin());
        assertNotNull(event.getParticipants());
        assertEquals(3, event.getParticipants().size());
        assertEquals("112233", event.getParticipants().get(1).getBroadcasterUserId());
        assertEquals("332211", event.getParticipants().get(2).getBroadcasterUserId());
    }

    @Test
    @DisplayName("Deserialize ChannelFollowEvent")
    public void deserializeSimpleUserChannelEvent() {
        ChannelFollowEvent event = jsonToObject(
            "{\"user_id\":\"1234\",\"user_login\":\"cool_user\",\"user_name\":\"Cool_User\",\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"cooler_user\",\"broadcaster_user_name\":\"Cooler_User\"}",
            ChannelFollowEvent.class
        );

        assertEquals("1234", event.getUserId());
        assertEquals("cool_user", event.getUserLogin());
        assertEquals("Cool_User", event.getUserName());
        assertEquals("1337", event.getBroadcasterUserId());
        assertEquals("cooler_user", event.getBroadcasterUserLogin());
        assertEquals("Cooler_User", event.getBroadcasterUserName());
    }

    @Test
    @DisplayName("Deserialize ChannelCheerEvent")
    public void deserializeComplexUserChannelEvent() {
        ChannelCheerEvent event = jsonToObject(
            "{\"is_anonymous\":false,\"user_id\":\"1234\",\"user_name\":\"cool_user\",\"broadcaster_user_id\":\"1337\",\"broadcaster_user_name\":\"cooler_user\",\"message\":\"pogchamp\",\"bits\":1000}",
            ChannelCheerEvent.class
        );

        assertEquals(false, event.isAnonymous());
        assertEquals("1234", event.getUserId());
        assertEquals("cool_user", event.getUserName());
        assertEquals("1337", event.getBroadcasterUserId());
        assertEquals("cooler_user", event.getBroadcasterUserName());
        assertEquals("pogchamp", event.getMessage());
        assertEquals(1000, event.getBits());
    }

    @Test
    @DisplayName("Deserialize ChannelSubscribeEvent")
    public void deserializeComplexUserChannelEvent2() {
        ChannelSubscribeEvent event = jsonToObject(
            "{\"user_id\":\"1234\",\"user_name\":\"cool_user\",\"broadcaster_user_id\":\"1337\",\"broadcaster_user_name\":\"cooler_user\",\"tier\":\"1000\",\"is_gift\":false}",
            ChannelSubscribeEvent.class
        );

        assertEquals("1234", event.getUserId());
        assertEquals("cool_user", event.getUserName());
        assertEquals("1337", event.getBroadcasterUserId());
        assertEquals("cooler_user", event.getBroadcasterUserName());
        assertEquals(SubscriptionPlan.TIER1, event.getTier());
        assertEquals(false, event.isGift());
    }

    @Test
    @DisplayName("Deserialize StreamOfflineEvent")
    public void deserializeSimpleChannelEvent() {
        StreamOfflineEvent event = jsonToObject(
            "{\"broadcaster_user_id\":\"1337\",\"broadcaster_user_name\":\"cool_user\"}",
            StreamOfflineEvent.class
        );

        assertEquals("1337", event.getBroadcasterUserId());
        assertEquals("cool_user", event.getBroadcasterUserName());
    }

    @Test
    @DisplayName("Deserialize StreamOnlineEvent")
    public void deserializeComplexStreamEvent() {
        StreamOnlineEvent event = jsonToObject(
            "{\"id\":\"9001\",\"broadcaster_user_id\":\"1337\",\"broadcaster_user_name\":\"cool_user\",\"type\":\"live\"}",
            StreamOnlineEvent.class
        );

        assertEquals("9001", event.getId());
        assertEquals("1337", event.getBroadcasterUserId());
        assertEquals("cool_user", event.getBroadcasterUserName());
        assertEquals(StreamType.LIVE, event.getType());
    }

    @Test
    @DisplayName("Deserialize ChannelUpdateV2Event")
    public void deserializeComplexChannelEvent() {
        ChannelUpdateV2Event event = jsonToObject(
            "{\"broadcaster_user_id\":\"1337\",\"broadcaster_user_name\":\"cool_user\",\"title\":\"Best Stream Ever\",\"language\":\"en\",\"category_id\":\"21779\",\"category_name\":\"Fortnite\",\"content_classification_labels\":[\"MatureGame\"]}",
            ChannelUpdateV2Event.class
        );

        assertEquals("1337", event.getBroadcasterUserId());
        assertEquals("cool_user", event.getBroadcasterUserName());
        assertEquals("Best Stream Ever", event.getTitle());
        assertEquals("en", event.getLanguage());
        assertEquals("21779", event.getCategoryId());
        assertEquals("Fortnite", event.getCategoryName());
        assertEquals(new HashSet<>(Collections.singletonList(ContentClassification.MATURE_GAME)), event.getContentClassificationLabels());
    }

    @Test
    @DisplayName("Deserialize CustomRewardAddEvent")
    public void deserializeSimpleRewardEvent() {
        String json = "{\"id\":\"9001\",\"broadcaster_user_id\":\"1337\",\"broadcaster_user_name\":\"cool_user\",\"is_enabled\":true,\"is_paused\":false,\"is_in_stock\":true,\"title\":\"Cool Reward\",\"cost\":100,\"prompt\":\"reward prompt\"," +
            "\"is_user_input_required\":true,\"should_redemptions_skip_request_queue\":false,\"cooldown_expires_at\":null,\"redemptions_redeemed_current_stream\":null,\"max_per_stream\":{\"is_enabled\":true,\"value\":1000}," +
            "\"max_per_user_per_stream\":{\"is_enabled\":true,\"value\":1000},\"global_cooldown\":{\"is_enabled\":true,\"seconds\":1000},\"background_color\":\"#FA1ED2\",\"image\":{\"url_1x\":\"https://static-cdn.jtvnw.net/image-1.png\"," +
            "\"url_2x\":\"https://static-cdn.jtvnw.net/image-2.png\",\"url_4x\":\"https://static-cdn.jtvnw.net/image-4.png\"},\"default_image\":{\"url_1x\":\"https://static-cdn.jtvnw.net/default-1.png\",\"url_2x\":\"https://static-cdn.jtvnw" +
            ".net/default-2.png\",\"url_4x\":\"https://static-cdn.jtvnw.net/default-4.png\"}}";

        CustomRewardAddEvent event = jsonToObject(json, CustomRewardAddEvent.class);
        assertEquals("9001", event.getId());
        assertEquals("1337", event.getBroadcasterUserId());
        assertEquals("cool_user", event.getBroadcasterUserName());
        assertEquals(true, event.isEnabled());
        assertEquals(false, event.isPaused());
        assertEquals(true, event.isInStock());
        assertEquals(100, event.getCost());
        assertNull(event.getCooldownExpiresAt());
        assertNotNull(event.getMaxPerStream());
        assertEquals(true, event.getMaxPerUserPerStream().isEnabled());
        assertEquals(1000, event.getMaxPerUserPerStream().getValue());
        assertNotNull(event.getDefaultImage());
        assertEquals("https://static-cdn.jtvnw.net/image-1.png", event.getImage().getUrl1x());
    }

    @Test
    @DisplayName("Deserialize CustomRewardRedemptionUpdateEvent")
    public void deserializeSimpleRedemptionEvent() {
        String json = "{\"id\":\"1234\",\"broadcaster_user_id\":\"1337\",\"broadcaster_user_name\":\"cool_user\",\"user_id\":\"9001\",\"user_name\":\"cooler_user\",\"user_input\":\"pogchamp\",\"status\":\"fulfilled\"," +
            "\"reward\":{\"id\":\"9001\",\"title\":\"title\",\"cost\":100,\"prompt\":\"reward prompt\"},\"redeemed_at\":\"2020-07-15T17:16:03.17106713Z\"}";

        CustomRewardRedemptionUpdateEvent event = jsonToObject(json, CustomRewardRedemptionUpdateEvent.class);
        assertEquals("1234", event.getId());
        assertEquals("1337", event.getBroadcasterUserId());
        assertEquals("cool_user", event.getBroadcasterUserName());
        assertEquals(RedemptionStatus.FULFILLED, event.getStatus());
        assertNotNull(event.getReward());
        assertEquals("9001", event.getReward().getId());
        assertEquals(100, event.getReward().getCost());
        assertNotNull(event.getRedeemedAt());
        assertEquals(Instant.parse("2020-07-15T17:16:03.17106713Z"), event.getRedeemedAt());
    }

    @Test
    @DisplayName("Deserialize HypeTrainProgressEvent")
    public void deserializeComplexHypeTrainEvent() {
        String json = "{\"broadcaster_user_id\":\"1337\",\"broadcaster_user_name\":\"cool_user\",\"level\":2,\"total\":700,\"progress\":200,\"goal\":1000," +
            "\"top_contributions\":[{\"user_id\":\"123\",\"user_name\":\"pogchamp\",\"type\":\"bits\",\"total\":50},{\"user_id\":\"456\",\"user_name\":\"kappa\",\"type\":\"subscription\",\"total\":45}]," +
            "\"last_contribution\":{\"user_id\":\"123\",\"user_name\":\"pogchamp\",\"type\":\"bits\",\"total\":50},\"started_at\":\"2020-07-15T17:16:03.17106713Z\",\"expires_at\":\"2020-07-15T17:16:11.17106713Z\"}";

        HypeTrainProgressEvent event = jsonToObject(json, HypeTrainProgressEvent.class);
        assertEquals("1337", event.getBroadcasterUserId());
        assertEquals("cool_user", event.getBroadcasterUserName());
        assertEquals(2, event.getLevel());
        assertEquals(700, event.getTotal());
        assertEquals(200, event.getProgress());
        assertEquals(1000, event.getGoal());
        assertNotNull(event.getLastContribution());
        assertNotNull(event.getTopContributions());
        assertEquals("123", event.getLastContribution().getUserId());
        assertEquals(50, event.getLastContribution().getTotal());
        assertEquals(Contribution.Type.BITS, event.getLastContribution().getType());
        assertNotNull(event.getStartedAt());
        assertEquals(Instant.parse("2020-07-15T17:16:11.17106713Z"), event.getExpiresAt());
    }

    @Test
    @DisplayName("Deserialize UserUpdateEvent")
    public void deserializeComplexUserEvent() {
        UserUpdateEvent event = jsonToObject(
            "{\"user_id\":\"1337\",\"user_name\":\"cool_user\",\"email\":\"user@email.com\",\"email_verified\":true,\"description\":\"cool description\"}",
            UserUpdateEvent.class
        );

        assertEquals("1337", event.getUserId());
        assertEquals("cool_user", event.getUserName());
        assertEquals("user@email.com", event.getEmail());
        assertTrue(event.isEmailVerified());
        assertEquals("cool description", event.getDescription());
    }

    @Test
    @DisplayName("Deserialize ChannelChatNotificationEvent where notice_type is announcement")
    public void deserializeChatAnnouncement() {
        ChannelChatNotificationEvent event = jsonToObject(
            "{\"broadcaster_user_id\":\"53888434\",\"broadcaster_user_login\":\"ogprodigy\",\"broadcaster_user_name\":\"OGprodigy\",\"chatter_user_id\":\"53888434\",\"chatter_user_login\":\"ogprodigy\",\"chatter_user_name\":\"OGprodigy\",\"chatter_is_anonymous\":false,\"color\":\"#00FF7F\",\"system_message\":\"\",\"message_id\":\"032cffb0-99ba-47cc-a903-23555b6137e2\",\"message\":{\"text\":\"test\",\"fragments\":[{\"type\":\"text\",\"text\":\"test\",\"cheermote\":null,\"emote\":null,\"mention\":null}]},\"notice_type\":\"announcement\",\"sub\":null,\"resub\":null,\"sub_gift\":null,\"community_sub_gift\":null,\"gift_paid_upgrade\":null,\"prime_paid_upgrade\":null,\"pay_it_forward\":null,\"raid\":null,\"unraid\":null,\"announcement\":{\"color\":\"PRIMARY\"},\"bits_badge_tier\":null,\"charity_donation\":null}",
            ChannelChatNotificationEvent.class
        );

        assertEquals(NoticeType.ANNOUNCEMENT, event.getNoticeType());
        assertFalse(event.isChatterAnonymous());
        assertEquals("53888434", event.getBroadcasterUserId());
        Message message = event.getMessage();
        assertNotNull(message);
        assertEquals("test", message.getText());
        List<Fragment> fragments = message.getFragments();
        assertNotNull(fragments);
        assertEquals(1, fragments.size());
        Fragment fragment = fragments.get(0);
        assertEquals(Fragment.Type.TEXT, fragment.getType());
        assertEquals("test", fragment.getText());
        Announcement announcement = event.getAnnouncement();
        assertNotNull(announcement);
        assertEquals(AnnouncementColor.PRIMARY, announcement.getColor());
    }

    @Test
    @DisplayName("Deserialize ChannelChatNotificationEvent where notice_type is resub")
    public void deserializeChatPrimeResub() {
        ChannelChatNotificationEvent event = jsonToObject(
            "{\"broadcaster_user_id\":\"207813352\",\"broadcaster_user_login\":\"hasanabi\",\"broadcaster_user_name\":\"HasanAbi\",\"chatter_user_id\":\"47525664\",\"chatter_user_login\":\"deetmonster\",\"chatter_user_name\":\"deetmonster\",\"chatter_is_anonymous\":false,\"color\":\"#0000FF\",\"system_message\":\"deetmonster subscribed with Prime. They've subscribed for 50 months, currently on a 12 month streak!\",\"message_id\":\"2e96f15c-db41-45f6-8e76-1d773ef3c47f\",\"message\":{\"text\":\"\",\"fragments\":[]},\"notice_type\":\"resub\",\"sub\":null,\"resub\":{\"cumulative_months\":50,\"duration_months\":0,\"streak_months\":12,\"sub_tier\":\"1000\",\"is_prime\":true,\"is_gift\":false,\"gifter_is_anonymous\":null,\"gifter_user_id\":null,\"gifter_user_name\":null,\"gifter_user_login\":null},\"sub_gift\":null,\"community_sub_gift\":null,\"gift_paid_upgrade\":null,\"prime_paid_upgrade\":null,\"pay_it_forward\":null,\"raid\":null,\"unraid\":null,\"announcement\":null,\"bits_badge_tier\":null,\"charity_donation\":null}",
            ChannelChatNotificationEvent.class
        );

        assertEquals(NoticeType.RESUB, event.getNoticeType());
        assertEquals("207813352", event.getBroadcasterUserId());
        assertEquals("47525664", event.getChatterUserId());
        assertFalse(event.isChatterAnonymous());
        assertEquals("deetmonster subscribed with Prime. They've subscribed for 50 months, currently on a 12 month streak!", event.getSystemMessage());
        Resubscription resub = event.getResub();
        assertNotNull(resub);
        assertEquals(50, resub.getCumulativeMonths());
        assertEquals(1, resub.getDurationMonths()); // we convert 0 to 1 intentionally
        assertEquals(12, resub.getStreakMonths());
        assertEquals(SubscriptionPlan.TIER1, resub.getSubTier());
        assertTrue(resub.isPrime());
        assertFalse(resub.isGift());
    }

    @Test
    @DisplayName("Deserialize Automod Message Hold Event")
    public void deserializeAutomodHeld() {
        AutomodMessageHoldEvent event = jsonToObject(
            "{\"broadcaster_user_id\":\"1234\",\"broadcaster_user_login\":\"redacted_channel\",\"broadcaster_user_name\":\"redacted_channel\",\"user_id\":\"6789\",\"user_login\":\"redacted_user\",\"user_name\":\"redacted_user\",\"message_id\":\"aa72e85e-77f5-4b7b-95e5-1e5efd8d8373\",\"message\":{\"text\":\"fuck israel\",\"fragments\":[{\"type\":\"text\",\"text\":\"fuck israel\",\"cheermote\":null,\"emote\":null}]},\"category\":\"racism\",\"level\":1,\"held_at\":\"2024-05-27T20:33:37.988487508Z\"}",
            AutomodMessageHoldEvent.class
        );

        assertEquals("1234", event.getBroadcasterUserId());
        assertEquals("6789", event.getUserId());
        assertEquals("aa72e85e-77f5-4b7b-95e5-1e5efd8d8373", event.getMessageId());
        assertEquals("fuck israel", event.getMessage().getText());
        assertEquals(AutomodCategory.RACISM, event.getCategory().getValue());
        assertEquals(1, event.getLevel());
        assertEquals(Instant.parse("2024-05-27T20:33:37.988487508Z"), event.getHeldAt());
        assertNotNull(event.getMessage().getFragments());
        assertEquals(1, event.getMessage().getFragments().size());
        Fragment fragment = event.getMessage().getFragments().get(0);
        assertEquals(Fragment.Type.TEXT, fragment.getType());
        assertEquals("fuck israel", fragment.getText());
    }

    @Test
    @DisplayName("Deserialize Automod Message Update Event")
    public void deserializeAutomodUpdate() {
        AutomodMessageUpdateEvent event = jsonToObject(
            "{\"broadcaster_user_id\":\"1234\",\"broadcaster_user_login\":\"redacted_channel\",\"broadcaster_user_name\":\"redacted_channel\",\"user_id\":\"6789\",\"user_login\":\"redacted_user\",\"user_name\":\"redacted_user\",\"moderator_user_id\":\"53888434\",\"moderator_user_login\":\"ogprodigy\",\"moderator_user_name\":\"OGprodigy\",\"message_id\":\"aa72e85e-77f5-4b7b-95e5-1e5efd8d8373\",\"message\":{\"text\":\"fuck israel\",\"fragments\":[{\"type\":\"text\",\"text\":\"fuck israel\",\"cheermote\":null,\"emote\":null}]},\"category\":\"racism\",\"level\":1,\"status\":\"approved\",\"held_at\":\"2024-05-27T20:33:37.988487508Z\"}",
            AutomodMessageUpdateEvent.class
        );

        assertEquals(AutomodMessageStatus.APPROVED, event.getStatus());
        assertEquals("53888434", event.getModeratorUserId());
        assertEquals("1234", event.getBroadcasterUserId());
        assertEquals("6789", event.getUserId());
        assertEquals("aa72e85e-77f5-4b7b-95e5-1e5efd8d8373", event.getMessageId());
        assertEquals("fuck israel", event.getMessage().getText());
        assertEquals(AutomodCategory.RACISM, event.getCategory().getValue());
        assertEquals(1, event.getLevel());
        assertEquals(Instant.parse("2024-05-27T20:33:37.988487508Z"), event.getHeldAt());
        assertNotNull(event.getMessage().getFragments());
        assertEquals(1, event.getMessage().getFragments().size());
        Fragment fragment = event.getMessage().getFragments().get(0);
        assertEquals(Fragment.Type.TEXT, fragment.getType());
        assertEquals("fuck israel", fragment.getText());
    }

}
