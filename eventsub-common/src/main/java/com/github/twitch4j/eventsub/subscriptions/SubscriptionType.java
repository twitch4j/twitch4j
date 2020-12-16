package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.EventSubCondition;
import com.github.twitch4j.helix.domain.EventSubSubscription;
import com.github.twitch4j.helix.eventsub.EventSubTransport;

import java.util.function.Function;

public interface SubscriptionType<C extends EventSubCondition, B> {

    /**
     * @return the subscription type name.
     */
    String getName();

    /**
     * @return the subscription type version.
     */
    String getVersion();

    /**
     * The parameters inside these objects differ by subscription type and may differ by version.
     *
     * @return Subscription-specific parameter builder.
     */
    B getConditionBuilder();

    @SuppressWarnings("unchecked")
    default Class<C> getConditionClass() {
        return (Class<C>) getConditionBuilder().getClass().getEnclosingClass();
    }

    default EventSubSubscription prepareSubscription(Function<B, C> conditions, EventSubTransport transport) {
        return EventSubSubscription.builder()
            .type(getName())
            .version(getVersion())
            .condition(conditions.apply(getConditionBuilder()).toMap())
            .transport(transport)
            .build();
    }

}
