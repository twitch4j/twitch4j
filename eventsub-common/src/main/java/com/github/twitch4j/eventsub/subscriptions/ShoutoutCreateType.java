package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ChannelShoutoutCondition;
import com.github.twitch4j.eventsub.events.ShoutoutCreateEvent;

/**
 * Sends a notification when the specified broadcaster sends a Shoutout.
 * <p>
 * Requires the moderator:read:shoutouts or moderator:manage:shoutouts scope.
 * <p>
 * If you use webhooks, the user in moderator_user_id must have granted your app (client ID)
 * one of the above permissions prior to your app subscribing to this subscription type.
 * <p>
 * If you use WebSockets, the ID in moderator_user_id must match the user ID in the user access token.
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_SHOUTOUTS_READ
 */
public class ShoutoutCreateType implements SubscriptionType<ChannelShoutoutCondition, ChannelShoutoutCondition.ChannelShoutoutConditionBuilder<?, ?>, ShoutoutCreateEvent> {

    @Override
    public String getName() {
        return "channel.shoutout.create";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public ChannelShoutoutCondition.ChannelShoutoutConditionBuilder<?, ?> getConditionBuilder() {
        return ChannelShoutoutCondition.builder();
    }

    @Override
    public Class<ShoutoutCreateEvent> getEventClass() {
        return ShoutoutCreateEvent.class;
    }

}
