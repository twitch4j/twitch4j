package com.github.twitch4j.eventsub.socket;

import com.github.twitch4j.eventsub.EventSubSubscription;
import com.github.twitch4j.eventsub.EventSubSubscriptionStatus;
import com.github.twitch4j.eventsub.EventSubTransport;
import com.github.twitch4j.eventsub.condition.EventSubCondition;
import com.github.twitch4j.eventsub.subscriptions.SubscriptionType;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Instant;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false, cacheStrategy = EqualsAndHashCode.CacheStrategy.LAZY)
class SubscriptionWrapper extends EventSubSubscription {
    EventSubSubscription subscription;

    private SubscriptionWrapper(EventSubSubscription sub) {
        super();
        this.subscription = sub;
    }

    @Override
    @EqualsAndHashCode.Include
    public String getRawType() {
        return subscription.getRawType();
    }

    @Override
    @EqualsAndHashCode.Include
    public String getRawVersion() {
        return subscription.getRawVersion();
    }

    @Override
    @EqualsAndHashCode.Include
    public EventSubCondition getCondition() {
        return subscription.getCondition();
    }

    @Override
    public String getId() {
        return subscription.getId();
    }

    @Override
    public EventSubSubscriptionStatus getStatus() {
        return subscription.getStatus();
    }

    @Override
    public SubscriptionType<?, ?, ?> getType() {
        return subscription.getType();
    }

    @Override
    public Instant getCreatedAt() {
        return subscription.getCreatedAt();
    }

    @Override
    public EventSubTransport getTransport() {
        return subscription.getTransport();
    }

    @Override
    @Deprecated
    public void setTransport(EventSubTransport transport) {
        subscription.setTransport(transport);
    }

    @Override
    public Integer getCost() {
        return subscription.getCost();
    }

    @Override
    public Boolean isBatchingEnabled() {
        return subscription.isBatchingEnabled();
    }

    static SubscriptionWrapper wrap(EventSubSubscription sub) {
        if (sub == null) return null;
        if (sub instanceof SubscriptionWrapper) return (SubscriptionWrapper) sub;
        return new SubscriptionWrapper(sub);
    }
}
