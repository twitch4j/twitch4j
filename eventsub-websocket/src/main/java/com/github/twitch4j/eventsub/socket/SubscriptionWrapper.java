package com.github.twitch4j.eventsub.socket;

import com.github.twitch4j.eventsub.EventSubSubscription;
import com.github.twitch4j.eventsub.condition.EventSubCondition;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
class SubscriptionWrapper extends EventSubSubscription {
    @EqualsAndHashCode.Exclude
    EventSubSubscription subscription;
    String rawType;
    String rawVersion;
    EventSubCondition condition;

    SubscriptionWrapper(EventSubSubscription sub) {
        super(sub.getId(), sub.getStatus(), sub.getType(), sub.getCondition(),
            sub.getCreatedAt(), sub.getTransport(), sub.getCost(), sub.isBatchingEnabled(),
            sub.getRawType(), sub.getRawVersion());
        this.subscription = sub;
        this.rawType = sub.getRawType();
        this.rawVersion = sub.getRawVersion();
        this.condition = sub.getCondition();
    }

    static SubscriptionWrapper wrap(EventSubSubscription sub) {
        if (sub == null) return null;
        if (sub instanceof SubscriptionWrapper) return (SubscriptionWrapper) sub;
        return new SubscriptionWrapper(sub);
    }
}
