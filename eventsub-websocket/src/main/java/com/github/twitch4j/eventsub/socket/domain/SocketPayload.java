package com.github.twitch4j.eventsub.socket.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.eventsub.EventSubSubscription;
import com.github.twitch4j.eventsub.events.EventSubEvent;
import com.github.twitch4j.eventsub.socket.enums.SocketMessageType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

/**
 * An object that contains the EventSub Websocket message.
 */
@Data
@Setter(AccessLevel.PRIVATE)
public class SocketPayload {

    /**
     * An object that contains information about the connection.
     * <p>
     * Present if {@link SocketMessageMetadata#getMessageType()} is:
     * {@link SocketMessageType#SESSION_WELCOME} or {@link SocketMessageType#SESSION_RECONNECT}.
     */
    @Nullable
    private EventSubSocketInformation session;

    /**
     * An object that contains information about your subscription.
     * <p>
     * Present if {@link SocketMessageMetadata#getMessageType()} is:
     * {@link SocketMessageType#NOTIFICATION} or {@link SocketMessageType#REVOCATION}.
     */
    @Nullable
    private EventSubSubscription subscription;

    /**
     * The event’s data.
     * <p>
     * For information about the event’s data, see the subscription type’s description
     * in <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types">Subscription Types</a>.
     * <p>
     * Present only if {@link SocketMessageMetadata#getMessageType()} is {@link SocketMessageType#NOTIFICATION}.
     */
    @Nullable
    @JsonProperty("event")
    private Object eventData;

    /**
     * @return the parsed event data from raw Map to {@link EventSubEvent}, or null if this payload does not represent a notification.
     */
    @Nullable
    public EventSubEvent getParsedEvent() {
        return subscription != null && eventData != null
            ? TypeConvert.convertValue(this.eventData, this.subscription.getType().getEventClass())
            : null;
    }

}
