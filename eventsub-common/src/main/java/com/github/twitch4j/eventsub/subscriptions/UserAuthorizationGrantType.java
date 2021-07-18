package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.UserAuthorizationGrantCondition;
import com.github.twitch4j.eventsub.events.UserAuthorizationGrantEvent;

/**
 * A user has granted authorization for your client id.
 * <p>
 * Provided client_id must match the client id in the application access token.
 */
public class UserAuthorizationGrantType implements SubscriptionType<UserAuthorizationGrantCondition, UserAuthorizationGrantCondition.UserAuthorizationGrantConditionBuilder<?, ?>, UserAuthorizationGrantEvent> {

    @Override
    public String getName() {
        return "user.authorization.grant";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public UserAuthorizationGrantCondition.UserAuthorizationGrantConditionBuilder<?, ?> getConditionBuilder() {
        return UserAuthorizationGrantCondition.builder();
    }

    @Override
    public Class<UserAuthorizationGrantEvent> getEventClass() {
        return UserAuthorizationGrantEvent.class;
    }

}
