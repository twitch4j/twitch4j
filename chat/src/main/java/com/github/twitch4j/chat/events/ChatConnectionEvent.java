package com.github.twitch4j.chat.events;

import com.github.philippheuer.events4j.core.domain.Event;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.client.websocket.domain.WebsocketConnectionState;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Chat Connection Event
 *
 * Called when a chat's connection changes.
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ChatConnectionEvent extends Event {

    /**
     * The state of the websocket
     */
    private final WebsocketConnectionState state;

    /**
     * The chat whose connection changed
     */
    private final TwitchChat chat;
}
