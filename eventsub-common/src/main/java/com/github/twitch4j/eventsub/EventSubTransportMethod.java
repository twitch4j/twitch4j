package com.github.twitch4j.eventsub;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EventSubTransportMethod {
    @JsonProperty("webhook")
    WEBHOOK,
    @JsonProperty("websocket")
    WEBSOCKET,
    @JsonProperty("conduit")
    CONDUIT;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
