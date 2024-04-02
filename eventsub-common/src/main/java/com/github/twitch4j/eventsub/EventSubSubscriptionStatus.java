package com.github.twitch4j.eventsub;

import com.github.twitch4j.common.annotation.Unofficial;

public enum EventSubSubscriptionStatus {

    /**
     * Designates that the subscription is in an operable state and is valid.
     */
    ENABLED,

    /**
     * Webhook is pending verification of the callback specified in the subscription creation request.
     */
    WEBHOOK_CALLBACK_VERIFICATION_PENDING,

    /**
     * Webhook failed verification of the callback specified in the subscription creation request.
     */
    WEBHOOK_CALLBACK_VERIFICATION_FAILED,

    /**
     * Notification delivery failure rate was too high.
     */
    NOTIFICATION_FAILURES_EXCEEDED,

    /**
     * Authorization for user(s) in the condition was revoked.
     */
    AUTHORIZATION_REVOKED,

    /**
     * The moderator that authorized the subscription is no longer one of the broadcaster’s moderators.
     */
    MODERATOR_REMOVED,

    /**
     * A user in the condition of the subscription was removed.
     */
    USER_REMOVED,

    /**
     * Twitch revoked your subscription because the subscribed to subscription type and version is no longer supported.
     */
    VERSION_REMOVED,

    /**
     * A beta eventsub subscription is temporarily not enabled due to maintenance.
     */
    BETA_MAINTENANCE,

    /**
     * Twitch revoked your subscription to a chat topic because your chatter user was banned by a moderator of the channel.
     */
    @Unofficial // https://github.com/twitchdev/issues/issues/931#issuecomment-2018532569
    CHAT_USER_BANNED,

    /**
     * When you connect to the server, you must create a subscription within 10 seconds or the connection is closed.
     * <p>
     * Note: The time limit is subject to change.
     */
    WEBSOCKET_CONNECTION_UNUSED,

    /**
     * The client closed the connection.
     */
    WEBSOCKET_DISCONNECTED,

    /**
     * You must respond to ping messages with a pong message.
     */
    WEBSOCKET_FAILED_PING_PONG,

    /**
     * A websocket conduit shard did not reconnect after being disconnected.
     */
    @Unofficial // https://github.com/twitchdev/issues/issues/931#issuecomment-2018532569
    WEBSOCKET_FAILED_TO_RECONNECT,

    /**
     * Indicates a problem with the server (similar to an HTTP 500 status code).
     */
    WEBSOCKET_INTERNAL_ERROR,

    /**
     * Transient network error.
     */
    WEBSOCKET_NETWORK_ERROR,

    /**
     * Transient network timeout.
     */
    WEBSOCKET_NETWORK_TIMEOUT,

    /**
     * Sending outgoing messages to the server is prohibited, except for pong messages.
     */
    WEBSOCKET_RECEIVED_INBOUND_TRAFFIC;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

}
