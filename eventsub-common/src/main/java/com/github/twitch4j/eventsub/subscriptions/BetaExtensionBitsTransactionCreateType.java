package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.eventsub.condition.ExtensionBitsTransactionCreateCondition;
import com.github.twitch4j.eventsub.events.ExtensionBitsTransactionCreateEvent;

/**
 * This subscription type sends a notification when a new transaction is created for a Twitch Extension.
 * <p>
 * Unless otherwise noted, EventSub subscriptions that were released as a public beta will be available for 30 days after their v1 version is released.
 * Subscriptions should be updated to v1 during this timeframe. Any active beta subscriptions beyond 30 days will be automatically deleted.
 */
@Unofficial
public class BetaExtensionBitsTransactionCreateType implements SubscriptionType<ExtensionBitsTransactionCreateCondition, ExtensionBitsTransactionCreateCondition.ExtensionBitsTransactionCreateConditionBuilder<?, ?>, ExtensionBitsTransactionCreateEvent> {

    @Override
    public String getName() {
        return "extension.bits_transaction.create";
    }

    @Override
    public String getVersion() {
        return "beta";
    }

    @Override
    public ExtensionBitsTransactionCreateCondition.ExtensionBitsTransactionCreateConditionBuilder<?, ?> getConditionBuilder() {
        return ExtensionBitsTransactionCreateCondition.builder();
    }

    @Override
    public Class<ExtensionBitsTransactionCreateEvent> getEventClass() {
        return ExtensionBitsTransactionCreateEvent.class;
    }

}
