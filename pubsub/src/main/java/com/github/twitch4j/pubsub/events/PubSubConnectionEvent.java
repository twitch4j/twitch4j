package com.github.twitch4j.pubsub.events;

import com.github.philippheuer.events4j.core.domain.Event;
import com.github.twitch4j.client.websocket.domain.WebsocketConnectionState;
import com.github.twitch4j.pubsub.TwitchPubSub;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * PubSub Connection Event
 * <p>
 * Called when a PubSub socket's connection status changes.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PubSubConnectionEvent extends Event {

    /**
     * The previous state of the websocket.
     */
    private final WebsocketConnectionState previousState;

    /**
     * The updated state of the websocket.
     */
    private final WebsocketConnectionState state;

    /**
     * The PubSub instance whose connection status changed.
     */
    private final TwitchPubSub chat;

}
