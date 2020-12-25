package com.github.twitch4j.eventsub;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.twitch4j.eventsub.condition.EventSubCondition;
import com.github.twitch4j.eventsub.events.EventSubEvent;
import com.github.twitch4j.eventsub.subscriptions.SubscriptionType;
import com.github.twitch4j.eventsub.util.NotificationDeserializer;
import com.github.twitch4j.helix.domain.EventSubSubscription;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonDeserialize(using = NotificationDeserializer.class)
public class EventSubNotification {

    private EventSubSubscription subscription;

    @ToString.Exclude
    private SubscriptionType<?, ?, ?> subscriptionType;

    @ToString.Exclude
    private EventSubCondition condition;

    private EventSubEvent event;

}
