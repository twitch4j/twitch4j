package com.github.twitch4j.eventsub.subscriptions;

import com.github.twitch4j.eventsub.condition.ModeratorEventSubCondition;
import com.github.twitch4j.eventsub.events.ChannelModerateEvent;

/**
 * Fires when a moderator performs a moderation action in a channel.
 * <p>
 * Must have all of the following scopes:
 * <ul>
 *     <li>moderator:read:blocked_terms OR moderator:manage:blocked_terms</li>
 *     <li>moderator:read:chat_settings OR moderator:manage:chat_settings</li>
 *     <li>moderator:read:unban_requests OR moderator:manage:unban_requests</li>
 *     <li>moderator:read:banned_users OR moderator:manage:banned_users</li>
 *     <li>moderator:read:chat_messages OR moderator:manage:chat_messages</li>
 *     <li>moderator:read:warnings OR moderator:manage:warnings</li>
 *     <li>moderator:read:moderators</li>
 *     <li>moderator:read:vips</li>
 * </ul>
 *
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_BLOCKED_TERMS_READ
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHAT_SETTINGS_READ
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_UNBAN_REQUESTS_READ
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_BANNED_USERS_READ
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHAT_MESSAGES_READ
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_MODERATORS_READ
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_VIPS_READ
 * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_WARNINGS_READ
 */
public class ChannelModerateType implements SubscriptionType<ModeratorEventSubCondition, ModeratorEventSubCondition.ModeratorEventSubConditionBuilder<?, ?>, ChannelModerateEvent> {
    @Override
    public String getName() {
        return "channel.moderate";
    }

    @Override
    public String getVersion() {
        return "2";
    }

    @Override
    public ModeratorEventSubCondition.ModeratorEventSubConditionBuilder<?, ?> getConditionBuilder() {
        return ModeratorEventSubCondition.builder();
    }

    @Override
    public Class<ChannelModerateEvent> getEventClass() {
        return ChannelModerateEvent.class;
    }
}
