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
            "{\"subscription\":{\"id\":\"f1c2a387-161a-49f9-a165-0f21d7a4e1c4\",\"status\":\"authorization_revoked\",\"type\":\"channel.follow\",\"version\":\"1\",\"condition\":{\"broadcaster_user_id\":\"12826\"},\"transport\":{\"method\":\"webhook\"," +
                "\"callback\":\"https://example.com/webhooks/callback\"},\"created_at\":\"2019-11-16T10:11:12.123Z\"},\"event\":{\"user_id\":\"1337\",\"user_name\":\"awesome_user\",\"broadcaster_user_id\":\"12826\",\"broadcaster_user_name\":\"twitch\"}}",
            EventSubNotification.class
        );

        assertNotNull(notif);
        assertNotNull(notif.getSubscription());
        assertEquals("f1c2a387-161a-49f9-a165-0f21d7a4e1c4", notif.getSubscription().getId());
        assertEquals(EventSubSubscriptionStatus.AUTHORIZATION_REVOKED, notif.getSubscription().getStatus());
        assertEquals(SubscriptionTypes.CHANNEL_FOLLOW, notif.getSubscription().getType());
        assertEquals(ChannelFollowCondition.builder().broadcasterUserId("12826").build(), notif.getSubscription().getCondition());
        assertNotNull(notif.getSubscription().getTransport());
        assertEquals(EventSubTransportMethod.WEBHOOK, notif.getSubscription().getTransport().getMethod());
        assertEquals("https://example.com/webhooks/callback", notif.getSubscription().getTransport().getCallback());
        assertEquals(Instant.parse("2019-11-16T10:11:12.123Z"), notif.getSubscription().getCreatedAt());
        assertTrue(notif.getEvent() instanceof ChannelFollowEvent);
        ChannelFollowEvent event = (ChannelFollowEvent) notif.getEvent();
        assertEquals("1337", event.getUserId());
        assertEquals("awesome_user", event.getUserName());
        assertEquals("12826", event.getBroadcasterUserId());
        assertEquals("twitch", event.getBroadcasterUserName());
    }

}
