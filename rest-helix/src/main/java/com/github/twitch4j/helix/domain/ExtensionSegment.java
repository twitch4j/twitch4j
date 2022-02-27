package com.github.twitch4j.helix.domain;

public enum ExtensionSegment {

    BROADCASTER,

    DEVELOPER,

    GLOBAL;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

}
