package com.github.twitch4j.eventsub.socket.events;

import com.github.twitch4j.eventsub.socket.TwitchEventSocket;
import com.github.twitch4j.eventsub.socket.domain.SocketCloseReason;
import lombok.Value;

/**
 * Called when Twitch decides to close our EventSocket.
 */
@Value
public class EventSocketClosedByTwitchEvent {

    /**
     * The reported reason for why Twitch closed the EventSocket connection.
     */
    SocketCloseReason reason;

    /**
     * The EventSocket instance whose connection status changed.
     */
    TwitchEventSocket connection;

}
