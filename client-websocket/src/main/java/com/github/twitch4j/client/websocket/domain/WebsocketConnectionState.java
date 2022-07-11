package com.github.twitch4j.client.websocket.domain;

import com.github.twitch4j.client.websocket.WebsocketConnection;

/**
 * The state of a {@link WebsocketConnection}.
 */
public enum WebsocketConnectionState {

    /**
     * The websocket is in the process of disconnecting after a call to {@link WebsocketConnection#disconnect()}.
     */
    DISCONNECTING,

    /**
     * The websocket has started its reconnection procedure after a call to {@link WebsocketConnection#reconnect()}.
     */
    RECONNECTING,

    /**
     * The websocket is (deliberately) fully disconnected.
     * <p>
     * This state will eventually be set after a call to {@link WebsocketConnection#disconnect()}.
     * This is also the initial state upon creation of {@link WebsocketConnection}.
     */
    DISCONNECTED,

    /**
     * The websocket has started its connection procedure after a call to {@link WebsocketConnection#connect()}.
     */
    CONNECTING,

    /**
     * The websocket has established a connection after completing the connection handshake procedure.
     */
    CONNECTED,

    /**
     * The websocket inadvertently lost connection.
     * <p>
     * This could occur for a variety of reasons including: network issues, firewall changes, or even a server-side crash.
     */
    LOST

}
