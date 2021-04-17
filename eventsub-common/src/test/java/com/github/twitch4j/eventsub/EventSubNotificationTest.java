package com.github.twitch4j.eventsub;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.eventsub.condition.ChannelFollowCondition;
import com.github.twitch4j.eventsub.events.ChannelFollowEvent;
import com.github.twitch4j.eventsub.subscriptions.SubscriptionTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unittest")
public class EventSubNotificationTest {

    @Test
    @DisplayName("Deserialize Challenge")
    public void deserializeChallenge() {
        String json = "{\"subscription\":{\"id\":\"b9ee086b-4ff1-4f11-8be8-9c93e3cd9515\",\"status\":\"webhook_callback_verification_pending\",\"type\":\"channel.follow\",\"version\":\"1\",\"condition\":{\"broadcaster_user_id\":\"207813352\"}," +
            "\"transport\":{\"method\":\"webhook\",\"callback\":\"https://twitch4j.ngrok.io\"},\"created_at\":\"2021-01-20T19:41:07.513199878Z\"},\"challenge\":\"sample-challenge\"}";

        EventSubNotification notif = TypeConvert.jsonToObject(json, EventSubNotification.class);
        assertEquals("sample-challenge", notif.getChallenge());
    }

    @Test
    @DisplayName("Deserialize Follow Notification")
    public void deserializeFollowNotification() {
        EventSubNotification notif = TypeConvert.jsonToObject(
            "{\"subscription\":{\"id\":\"f1c2a387-161a-49f9-a165-0f21d7a4e1c4\",\"type\":\"channel.follow\",\"version\":\"1\",\"status\":\"enabled\",\"condition\":{\"broadcaster_user_id\":\"1337\"},\"transport\":{\"method\":\"webhook\"," +
                "\"callback\":\"https://example.com/webhooks/callback\"},\"created_at\":\"2019-11-16T10:11:12.123Z\"},\"event\":{\"user_id\":\"1234\",\"user_login\":\"cool_user\",\"user_name\":\"Cool_User\",\"broadcaster_user_id\":\"1337\"," +
                "\"broadcaster_user_login\":\"cooler_user\",\"broadcaster_user_name\":\"Cooler_User\",\"followed_at\":\"2020-07-15T18:16:11.17106713Z\"}}",
            EventSubNotification.class
        );

        assertNotNull(notif);
        assertNotNull(notif.getSubscription());
        assertEquals("f1c2a387-161a-49f9-a165-0f21d7a4e1c4", notif.getSubscription().getId());
        assertEquals(EventSubSubscriptionStatus.ENABLED, notif.getSubscription().getStatus());
        assertEquals(SubscriptionTypes.CHANNEL_FOLLOW, notif.getSubscription().getType());
        assertEquals(ChannelFollowCondition.builder().broadcasterUserId("1337").build(), notif.getSubscription().getCondition());
        assertNotNull(notif.getSubscription().getTransport());
        assertEquals(EventSubTransportMethod.WEBHOOK, notif.getSubscription().getTransport().getMethod());
        assertEquals("https://example.com/webhooks/callback", notif.getSubscription().getTransport().getCallback());
        assertEquals(Instant.parse("2019-11-16T10:11:12.123Z"), notif.getSubscription().getCreatedAt());
        assertTrue(notif.getEvent() instanceof ChannelFollowEvent);
        ChannelFollowEvent event = (ChannelFollowEvent) notif.getEvent();
        assertEquals("1234", event.getUserId());
        assertEquals("cool_user", event.getUserLogin());
        assertEquals("Cool_User", event.getUserName());
        assertEquals("1337", event.getBroadcasterUserId());
        assertEquals("cooler_user", event.getBroadcasterUserLogin());
        assertEquals("Cooler_User", event.getBroadcasterUserName());
        assertEquals(Instant.parse("2020-07-15T18:16:11.17106713Z"), event.getFollowedAt());
    }

}
