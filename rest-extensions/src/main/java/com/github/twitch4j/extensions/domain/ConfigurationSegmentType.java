package com.github.twitch4j.extensions.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ConfigurationSegmentType {

    @JsonProperty("global")
    GLOBAL,
    @JsonProperty("developer")
    DEVELOPER,
    @JsonProperty("broadcaster")
    BROADCASTER;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

}
