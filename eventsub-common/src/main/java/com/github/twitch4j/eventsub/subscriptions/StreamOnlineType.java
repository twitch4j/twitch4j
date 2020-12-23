package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.StreamOnlineCondition;
import com.github.twitch4j.eventsub.events.StreamOnlineEvent;

/**
 * The specified broadcaster starts a stream.
 * <p>
 * No authorization required.
 */
public class StreamOnlineType implements SubscriptionType<StreamOnlineCondition, StreamOnlineCondition.StreamOnlineConditionBuilder<?, ?>, StreamOnlineEvent> {

    @Override
    public String getName() {
        return "stream.online";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public StreamOnlineCondition.StreamOnlineConditionBuilder<?, ?> getConditionBuilder() {
        return StreamOnlineCondition.builder();
    }

    @Override
    public Class<StreamOnlineEvent> getEventClass() {
        return StreamOnlineEvent.class;
    }

}
