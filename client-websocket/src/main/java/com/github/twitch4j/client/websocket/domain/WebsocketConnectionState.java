package com.github.twitch4j.client.websocket.domain;

public enum WebsocketConnectionState {
    DISCONNECTING,
    RECONNECTING,
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    LOST
}
