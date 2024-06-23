package com.github.twitch4j.pubsub.domain;

import com.github.twitch4j.common.util.TypeConvert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unittest")
class AutomaticRedemptionTest {

    @Test
    void deserializeCelebration() {
        AutomaticRewardRedemption redemption = TypeConvert.jsonToObject(
            "{\"id\":\"9a78a874-1f9a-4373-b5e7-6c5943f37ec8\",\"user\":{\"id\":\"248530368\",\"login\":\"displayresolutions\",\"display_name\":\"DisplayResolutions\"},\"channel_id\":\"22484632\",\"redeemed_at\":\"2024-06-13T21:36:49Z\",\"reward\":{\"channel_id\":\"22484632\",\"reward_type\":\"CELEBRATION\",\"cost\":0,\"default_cost\":0,\"min_cost\":0,\"image\":null,\"default_image\":{\"url_1x\":\"https://static-cdn.jtvnw.net/automatic-reward-images/flame-1.png\",\"url_2x\":\"https://static-cdn.jtvnw.net/automatic-reward-images/flame-2.png\",\"url_4x\":\"https://static-cdn.jtvnw.net/automatic-reward-images/flame-4.png\"},\"background_color\":null,\"default_background_color\":\"#6EC46D\",\"is_enabled\":true,\"is_hidden_for_subs\":false,\"updated_for_indicator_at\":null,\"globally_updated_for_indicator_at\":\"2024-04-02T21:00:00Z\",\"is_in_stock\":true,\"max_per_stream\":{\"is_enabled\":false,\"max_per_stream\":0},\"max_per_user_per_stream\":{\"is_enabled\":false,\"max_per_user_per_stream\":0},\"global_cooldown\":{\"is_enabled\":false,\"global_cooldown_seconds\":0},\"cooldown_expires_at\":null,\"redemptions_redeemed_current_stream\":null,\"default_bits_cost\":120,\"bits_cost\":0,\"pricing_type\":\"BITS\"},\"redemption_metadata\":{\"celebration_emote_metadata\":{\"emote\":{\"id\":\"305660128\",\"token\":\"dakiTeam1\"}}}}",
            AutomaticRewardRedemption.class
        );
        assertEquals("9a78a874-1f9a-4373-b5e7-6c5943f37ec8", redemption.getId());
        assertNotNull(redemption.getUser());
        assertEquals("248530368", redemption.getUser().getId());
        assertEquals("DisplayResolutions", redemption.getUser().getDisplayName());
        assertEquals("22484632", redemption.getChannelId());
        assertEquals(Instant.parse("2024-06-13T21:36:49Z"), redemption.getRedeemedAt());

        assertNotNull(redemption.getRedemptionMetadata());
        assertNotNull(redemption.getRedemptionMetadata().getCelebrationEmoteMetadata());
        Emote emote = redemption.getRedemptionMetadata().getCelebrationEmoteMetadata().getEmote();
        assertNotNull(emote);
        assertEquals("305660128", emote.getId());
        assertEquals("dakiTeam1", emote.getToken());

        AutomaticReward reward = redemption.getReward();
        assertNotNull(reward);
        assertEquals(AutomaticReward.RewardType.CELEBRATION, reward.getRewardType());
        assertNotNull(reward.getDefaultImage());
        assertEquals("https://static-cdn.jtvnw.net/automatic-reward-images/flame-4.png", reward.getDefaultImage().getUrl4x());
        assertEquals("#6EC46D", reward.getDefaultBackgroundColor());
        assertTrue(reward.isEnabled());
        assertTrue(reward.isInStock());
        assertFalse(reward.isHiddenForSubs());
        assertTrue(reward.isBitsRedemption());
        assertEquals(120, reward.getDefaultBitsCost());
    }

    @Test
    void deserializeEffects() {
        AutomaticRewardRedemption redemption = TypeConvert.jsonToObject(
            "{\"id\":\"ba7c9854-53d6-4902-9673-8e892af8ba8e\",\"user\":{\"id\":\"115237649\",\"login\":\"realduu_\",\"display_name\":\"realDuu_\"},\"channel_id\":\"22484632\",\"redeemed_at\":\"2024-06-13T21:34:45Z\",\"reward\":{\"channel_id\":\"22484632\",\"reward_type\":\"SEND_ANIMATED_MESSAGE\",\"cost\":0,\"default_cost\":0,\"min_cost\":0,\"image\":null,\"default_image\":{\"url_1x\":\"https://static-cdn.jtvnw.net/automatic-reward-images/comments-1.png\",\"url_2x\":\"https://static-cdn.jtvnw.net/automatic-reward-images/comments-2.png\",\"url_4x\":\"https://static-cdn.jtvnw.net/automatic-reward-images/comments-4.png\"},\"background_color\":null,\"default_background_color\":\"#F177C1\",\"is_enabled\":true,\"is_hidden_for_subs\":false,\"updated_for_indicator_at\":null,\"globally_updated_for_indicator_at\":\"2024-04-02T21:00:00Z\",\"is_in_stock\":true,\"max_per_stream\":{\"is_enabled\":false,\"max_per_stream\":0},\"max_per_user_per_stream\":{\"is_enabled\":false,\"max_per_user_per_stream\":0},\"global_cooldown\":{\"is_enabled\":false,\"global_cooldown_seconds\":0},\"cooldown_expires_at\":null,\"redemptions_redeemed_current_stream\":null,\"default_bits_cost\":60,\"bits_cost\":0,\"pricing_type\":\"BITS\"},\"redemption_metadata\":{\"send_animated_message_metadata\":{\"animation_id\":\"rainbow-eclipse\"}}}",
            AutomaticRewardRedemption.class
        );
        assertEquals("ba7c9854-53d6-4902-9673-8e892af8ba8e", redemption.getId());
        assertNotNull(redemption.getUser());
        assertEquals("115237649", redemption.getUser().getId());
        assertEquals("realDuu_", redemption.getUser().getDisplayName());
        assertEquals("22484632", redemption.getChannelId());
        assertEquals(Instant.parse("2024-06-13T21:34:45Z"), redemption.getRedeemedAt());

        assertNotNull(redemption.getRedemptionMetadata());
        assertNotNull(redemption.getRedemptionMetadata().getSendAnimatedMessageMetadata());
        assertEquals("rainbow-eclipse", redemption.getRedemptionMetadata().getSendAnimatedMessageMetadata().getAnimationId());

        AutomaticReward reward = redemption.getReward();
        assertNotNull(reward);
        assertEquals(AutomaticReward.RewardType.SEND_ANIMATED_MESSAGE, reward.getRewardType());
        assertNotNull(reward.getDefaultImage());
        assertEquals("https://static-cdn.jtvnw.net/automatic-reward-images/comments-4.png", reward.getDefaultImage().getUrl4x());
        assertEquals("#F177C1", reward.getDefaultBackgroundColor());
        assertTrue(reward.isEnabled());
        assertTrue(reward.isInStock());
        assertFalse(reward.isHiddenForSubs());
        assertTrue(reward.isBitsRedemption());
        assertEquals(60, reward.getDefaultBitsCost());
    }

    @Test
    void deserializeGigantify() {
        AutomaticRewardRedemption redemption = TypeConvert.jsonToObject(
            "{\"id\":\"f68ad798-29a7-43b5-8faa-2fc388009f42\",\"user\":{\"id\":\"47614615\",\"login\":\"kvol_\",\"display_name\":\"kvol_\"},\"channel_id\":\"22484632\",\"redeemed_at\":\"2024-06-13T21:35:23Z\",\"reward\":{\"channel_id\":\"22484632\",\"reward_type\":\"SEND_GIGANTIFIED_EMOTE\",\"cost\":0,\"default_cost\":0,\"min_cost\":0,\"image\":null,\"default_image\":{\"url_1x\":\"https://static-cdn.jtvnw.net/automatic-reward-images/emote_add-1.png\",\"url_2x\":\"https://static-cdn.jtvnw.net/automatic-reward-images/emote_add-2.png\",\"url_4x\":\"https://static-cdn.jtvnw.net/automatic-reward-images/emote_add-4.png\"},\"background_color\":null,\"default_background_color\":\"#69B6FF\",\"is_enabled\":true,\"is_hidden_for_subs\":false,\"updated_for_indicator_at\":null,\"globally_updated_for_indicator_at\":\"2024-04-02T21:00:00Z\",\"is_in_stock\":true,\"max_per_stream\":{\"is_enabled\":false,\"max_per_stream\":0},\"max_per_user_per_stream\":{\"is_enabled\":false,\"max_per_user_per_stream\":0},\"global_cooldown\":{\"is_enabled\":false,\"global_cooldown_seconds\":0},\"cooldown_expires_at\":null,\"redemptions_redeemed_current_stream\":null,\"default_bits_cost\":100,\"bits_cost\":0,\"pricing_type\":\"BITS\"},\"redemption_metadata\":{\"send_gigantified_emote_metadata\":{\"emote\":{\"id\":\"emotesv2_b7482780923442c499ae7b4706040695\",\"token\":\"forsenInsane\"}}}}",
            AutomaticRewardRedemption.class
        );
        assertEquals("f68ad798-29a7-43b5-8faa-2fc388009f42", redemption.getId());
        assertNotNull(redemption.getUser());
        assertEquals("47614615", redemption.getUser().getId());
        assertEquals("kvol_", redemption.getUser().getDisplayName());
        assertEquals("22484632", redemption.getChannelId());
        assertEquals(Instant.parse("2024-06-13T21:35:23Z"), redemption.getRedeemedAt());

        assertNotNull(redemption.getRedemptionMetadata());
        assertNotNull(redemption.getRedemptionMetadata().getSendGigantifiedEmoteMetadata());
        Emote emote = redemption.getRedemptionMetadata().getSendGigantifiedEmoteMetadata().getEmote();
        assertNotNull(emote);
        assertEquals("emotesv2_b7482780923442c499ae7b4706040695", emote.getId());
        assertEquals("forsenInsane", emote.getToken());

        AutomaticReward reward = redemption.getReward();
        assertNotNull(reward);
        assertEquals(AutomaticReward.RewardType.SEND_GIGANTIFIED_EMOTE, reward.getRewardType());
        assertNotNull(reward.getDefaultImage());
        assertEquals("https://static-cdn.jtvnw.net/automatic-reward-images/emote_add-4.png", reward.getDefaultImage().getUrl4x());
        assertEquals("#69B6FF", reward.getDefaultBackgroundColor());
        assertTrue(reward.isEnabled());
        assertTrue(reward.isInStock());
        assertFalse(reward.isHiddenForSubs());
        assertTrue(reward.isBitsRedemption());
        assertEquals(100, reward.getDefaultBitsCost());
    }

}
