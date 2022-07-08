package com.github.twitch4j.chat.events;

import com.github.philippheuer.events4j.core.domain.Event;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.client.websocket.domain.WebsocketConnectionState;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Chat Connection Event
 * <p>
 * Called when a chat socket's connection status changes.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ChatConnectionEvent extends Event {

    /**
     * The previous state of the websocket.
     */
    private final WebsocketConnectionState previousState;

    /**
     * The updated state of the websocket.
     */
    private final WebsocketConnectionState state;

    /**
     * The chat instance whose connection status changed.
     */
    private final TwitchChat chat;

}
