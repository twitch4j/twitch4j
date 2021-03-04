package com.github.twitch4j.eventsub.events;

import com.github.twitch4j.common.enums.SubscriptionPlan;
import com.github.twitch4j.eventsub.domain.Contribution;
import com.github.twitch4j.eventsub.domain.RedemptionStatus;
import com.github.twitch4j.eventsub.domain.StreamType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.github.twitch4j.common.util.TypeConvert.jsonToObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unittest")
public class EventSubEventTest {

    @Test
    @DisplayName("Deserialize ChannelBanEvent: Timeout")
    public void deserializeModerationEvent() {
        ChannelBanEvent event = jsonToObject(
            "{\"user_id\":\"1234\",\"user_login\":\"cool_user\",\"user_name\":\"Cool_User\",\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"cooler_user\",\"broadcaster_user_name\":\"Cooler_User\",\"moderator_user_id\":\"1339\"," +
                "\"moderator_user_login\":\"mod_user\",\"moderator_user_name\":\"Mod_User\",\"reason\":\"Offensive language\",\"ends_at\":\"2020-07-15T18:16:11.17106713Z\",\"is_permanent\":false}",
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
    @DisplayName("Deserialize ChannelUpdateEvent")
    public void deserializeComplexChannelEvent() {
        ChannelUpdateEvent event = jsonToObject(
            "{\"broadcaster_user_id\":\"1337\",\"broadcaster_user_name\":\"cool_user\",\"title\":\"Best Stream Ever\",\"language\":\"en\",\"category_id\":\"21779\",\"category_name\":\"Fortnite\",\"is_mature\":false}",
            ChannelUpdateEvent.class
        );

        assertEquals("1337", event.getBroadcasterUserId());
        assertEquals("cool_user", event.getBroadcasterUserName());
        assertEquals("Best Stream Ever", event.getTitle());
        assertEquals("en", event.getLanguage());
        assertEquals("21779", event.getCategoryId());
        assertEquals("Fortnite", event.getCategoryName());
        assertEquals(false, event.isMature());
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
            "{\"user_id\":\"1337\",\"user_name\":\"cool_user\",\"email\":\"user@email.com\",\"description\":\"cool description\"}",
            UserUpdateEvent.class
        );

        assertEquals("1337", event.getUserId());
        assertEquals("cool_user", event.getUserName());
        assertEquals("user@email.com", event.getEmail());
        assertEquals("cool description", event.getDescription());
    }

}
