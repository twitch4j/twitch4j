package com.github.twitch4j.client.websocket.events;

import com.github.philippheuer.events4j.core.domain.Event;
import com.github.twitch4j.client.websocket.domain.WebsocketConnectionState;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Websocket State Event
 * <p>
 * Called when the websocket changes its connection state.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WebsocketStateEvent extends Event {

    /**
     * The state of the websocket
     */
    private final WebsocketConnectionState state;

    /**
     * The parent of the websocket
     */
    private final AutoCloseable parent;

}
