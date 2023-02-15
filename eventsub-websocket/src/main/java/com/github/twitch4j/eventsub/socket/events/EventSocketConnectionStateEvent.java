package com.github.twitch4j.eventsub.socket.events;

import com.github.twitch4j.client.websocket.domain.WebsocketConnectionState;
import com.github.twitch4j.eventsub.socket.TwitchEventSocket;
import lombok.Value;

/**
 * Called when the connection state of an EventSocket changes.
 */
@Value
public class EventSocketConnectionStateEvent {

    /**
     * The previous state of the websocket.
     */
    WebsocketConnectionState previousState;

    /**
     * The updated state of the websocket.
     */
    WebsocketConnectionState state;

    /**
     * The EventSocket instance whose connection status changed.
     */
    TwitchEventSocket connection;

}
