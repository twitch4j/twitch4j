package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.UserUpdateCondition;
import com.github.twitch4j.eventsub.events.UserUpdateEvent;

/**
 * A user has updated their account.
 * <p>
 * No authorization required. If you have the user:read:email scope, the notification will include email field.
 */
public class UserUpdateType implements SubscriptionType<UserUpdateCondition, UserUpdateCondition.UserUpdateConditionBuilder<?, ?>, UserUpdateEvent> {

    @Override
    public String getName() {
        return "user.update";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public UserUpdateCondition.UserUpdateConditionBuilder<?, ?> getConditionBuilder() {
        return UserUpdateCondition.builder();
    }

    @Override
    public Class<UserUpdateEvent> getEventClass() {
        return UserUpdateEvent.class;
    }

}
