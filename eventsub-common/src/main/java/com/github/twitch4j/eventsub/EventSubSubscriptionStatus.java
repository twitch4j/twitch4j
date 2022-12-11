package com.github.twitch4j.eventsub;

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
     * A user in the condition of the subscription was removed.
     */
    USER_REMOVED,

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
