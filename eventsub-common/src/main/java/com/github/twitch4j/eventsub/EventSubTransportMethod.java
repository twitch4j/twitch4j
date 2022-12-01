package com.github.twitch4j.eventsub;

public enum EventSubTransportMethod {
    WEBHOOK,
    WEBSOCKET;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
