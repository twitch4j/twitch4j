package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ConduitCondition;
import com.github.twitch4j.eventsub.events.ConduitShardDisabledEvent;

/**
 * The conduit.shard.disabled subscription type sends a notification when EventSub disables a shard
 * due to the status of the underlying transport changing.
 * <p>
 * App access token where the client ID matches the client ID in the condition.
 * If {@code conduit_id} is specified, the client must be the owner of the conduit.
 */
public class ConduitShardDisabledType implements SubscriptionType<ConduitCondition, ConduitCondition.ConduitConditionBuilder<?, ?>, ConduitShardDisabledEvent> {
    @Override
    public String getName() {
        return "conduit.shard.disabled";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ConduitCondition.ConduitConditionBuilder<?, ?> getConditionBuilder() {
        return ConduitCondition.builder();
    }

    @Override
    public Class<ConduitShardDisabledEvent> getEventClass() {
        return ConduitShardDisabledEvent.class;
    }
}
