package com.github.twitch4j.socket.enums;

import com.fasterxml.jackson.annotation.JsonAlias;

public enum EventSubSocketStatus {
    CONNECTED,
    RECONNECTING,
    @JsonAlias("client_disconnected")
    DISCONNECTED;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
