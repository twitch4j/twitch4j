package com.github.twitch4j.eventsub.socket.enums;

public enum SocketMessageType {
    SESSION_WELCOME,
    SESSION_KEEPALIVE,
    SESSION_RECONNECT,
    NOTIFICATION,
    REVOCATION;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
