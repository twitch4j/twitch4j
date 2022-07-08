package com.github.twitch4j.pubsub.events;

import com.github.philippheuer.events4j.core.domain.Event;
import com.github.twitch4j.client.websocket.domain.WebsocketConnectionState;
import com.github.twitch4j.pubsub.TwitchPubSub;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * PubSub Connection Event
 *
 * Called when a pubsub's connection changes.
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class PubSubConnectionEvent extends Event {

    /**
     * The state of the websocket
     */
    private final WebsocketConnectionState state;

    /**
     * The pubsub whose connection changed
     */
    private final TwitchPubSub chat;
}
