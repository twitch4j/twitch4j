package com.github.twitch4j.eventsub.socket.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class EventSubSocketMessage {

    /**
     * An object that identifies the message.
     */
    private SocketMessageMetadata metadata;

    /**
     * An object that contains the message.
     */
    private SocketPayload payload;

}
