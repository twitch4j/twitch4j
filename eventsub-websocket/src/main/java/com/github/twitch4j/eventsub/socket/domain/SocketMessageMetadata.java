package com.github.twitch4j.eventsub.socket.domain;

import com.github.twitch4j.eventsub.subscriptions.SubscriptionType;
import com.github.twitch4j.eventsub.subscriptions.SubscriptionTypes;
import com.github.twitch4j.eventsub.socket.enums.SocketMessageType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

/**
 * An object that identifies the message sent over EventSub Websockets.
 */
@Data
@Setter(AccessLevel.PRIVATE)
public class SocketMessageMetadata {

    /**
     * An ID that uniquely identifies the message.
     * <p>
     * Twitch sends messages at least once, but if Twitch is unsure of whether you received a notification, it’ll resend the message.
     * This means you may receive a notification twice. If Twitch resends the message, the message ID will be the same.
     */
    private String messageId;

    /**
     * The type of message.
     */
    private SocketMessageType messageType;

    /**
     * The UTC date and time that the message was sent.
     */
    private Instant messageTimestamp;

    /**
     * The type of event sent in the message.
     * <p>
     * Only present if {@link #getMessageType()} is {@link SocketMessageType#NOTIFICATION} or {@link SocketMessageType#REVOCATION}.
     */
    @Nullable
    private String subscriptionType;

    /**
     * The version number of the subscription type’s definition.
     * <p>
     * This is the same value specified in the subscription request.
     * <p>
     * Only present if {@link #getMessageType()} is {@link SocketMessageType#NOTIFICATION} or {@link SocketMessageType#REVOCATION}.
     */
    @Nullable
    private String subscriptionVersion;

    /**
     * @return the parsed {@link SubscriptionType} associated with the raw type and version contained in this metadata for a notification or revocation.
     */
    @Nullable
    public SubscriptionType<?, ?, ?> getParsedSubscriptionType() {
        return this.subscriptionType != null
            ? SubscriptionTypes.getSubscriptionType(this.subscriptionType, this.subscriptionVersion)
            : null;
    }

}
