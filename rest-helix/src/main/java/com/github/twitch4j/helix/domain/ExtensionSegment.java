package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ExtensionSegment {

    @JsonProperty("broadcaster")
    BROADCASTER,

    @JsonProperty("developer")
    DEVELOPER,

    @JsonProperty("global")
    GLOBAL;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

}
