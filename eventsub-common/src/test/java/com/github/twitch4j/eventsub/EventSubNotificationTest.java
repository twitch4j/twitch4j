package com.github.twitch4j.eventsub;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.eventsub.condition.ChannelFollowCondition;
import com.github.twitch4j.eventsub.events.ChannelFollowEvent;
import com.github.twitch4j.eventsub.subscriptions.SubscriptionTypes;
import com.github.twitch4j.helix.eventsub.EventSubSubscriptionStatus;
import com.github.twitch4j.helix.eventsub.EventSubTransportMethod;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("integration")
public class EventSubNotificationTest {

    @Test
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
        assertEquals(SubscriptionTypes.CHANNEL_FOLLOW, notif.getSubscriptionType());
        assertEquals(ChannelFollowCondition.builder().broadcasterUserId("12826").build(), notif.getCondition());
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
