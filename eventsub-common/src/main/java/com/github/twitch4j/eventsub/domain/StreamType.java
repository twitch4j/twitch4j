package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum StreamType {
    @JsonProperty("live") LIVE,
    @JsonProperty("playlist") PLAYLIST,
    @JsonProperty("watch_party") WATCH_PARTY,
    @JsonProperty("premiere") PREMIERE,
    @JsonProperty("rerun") RERUN;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
