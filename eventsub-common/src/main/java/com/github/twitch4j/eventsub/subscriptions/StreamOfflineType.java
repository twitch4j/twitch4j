package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.StreamOfflineCondition;
import com.github.twitch4j.eventsub.events.StreamOfflineEvent;

/**
 * The specified broadcaster stops a stream.
 * <p>
 * No authorization required.
 */
public class StreamOfflineType implements SubscriptionType<StreamOfflineCondition, StreamOfflineCondition.StreamOfflineConditionBuilder<?, ?>, StreamOfflineEvent> {

    @Override
    public String getName() {
        return "stream.offline";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public StreamOfflineCondition.StreamOfflineConditionBuilder<?, ?> getConditionBuilder() {
        return StreamOfflineCondition.builder();
    }

    @Override
    public Class<StreamOfflineEvent> getEventClass() {
        return StreamOfflineEvent.class;
    }

}
