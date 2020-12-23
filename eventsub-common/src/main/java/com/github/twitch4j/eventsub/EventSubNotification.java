package com.github.twitch4j.eventsub;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.eventsub.condition.EventSubCondition;
import com.github.twitch4j.eventsub.events.EventSubEvent;
import com.github.twitch4j.eventsub.subscriptions.SubscriptionType;
import com.github.twitch4j.eventsub.subscriptions.SubscriptionTypes;
import com.github.twitch4j.eventsub.util.EventSubConditionConverter;
import com.github.twitch4j.helix.domain.EventSubSubscription;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventSubNotification {

    private EventSubSubscription subscription;

    @ToString.Exclude
    @JsonProperty("event")
    @Getter(value = AccessLevel.PROTECTED)
    private Object rawEvent;

    @ToString.Exclude
    @Getter(lazy = true)
    private final SubscriptionType<?, ?, ?> subscriptionType = SubscriptionTypes.getSubscriptionType(subscription.getType(), subscription.getVersion());

    @JsonIgnore
    @Getter(lazy = true)
    private final EventSubEvent event = TypeConvert.convertValue(rawEvent, getSubscriptionType().getEventClass());

    @JsonIgnore
    @ToString.Exclude
    @Getter(lazy = true)
    private final EventSubCondition condition = EventSubConditionConverter.getCondition(getSubscriptionType(), subscription.getCondition());

}
