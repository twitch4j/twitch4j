package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SuspiciousTreatment {

    /**
     * Messages sent by monitored accounts will appear in chat,
     * but flagged in the chat panel and Mod View widget for moderators and streamers to review.
     */
    MONITOR("ACTIVE_MONITORING"),

    /**
     * Messages sent by restricted accounts will be displayed only to the streamer and their moderators.
     */
    RESTRICT("RESTRICTED");

    private final String twitchString;

    @Override
    @JsonValue
    public String toString() {
        return this.twitchString;
    }
}
