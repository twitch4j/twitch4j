package com.github.twitch4j.eventsub.socket.enums;

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
