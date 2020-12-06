package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.UserAuthorizationRevokeCondition;

/**
 * A user has revoked authorization for your client id.
 * <p>
 * Provided client_id must match the client id in the application access token.
 */
public class UserAuthorizationRevokeType implements SubscriptionType<UserAuthorizationRevokeCondition, UserAuthorizationRevokeCondition.UserAuthorizationRevokeConditionBuilder<?, ?>> {

    @Override
    public String getName() {
        return "user.authorization.revoke";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public UserAuthorizationRevokeCondition.UserAuthorizationRevokeConditionBuilder<?, ?> getConditionBuilder() {
        return UserAuthorizationRevokeCondition.builder();
    }

}
