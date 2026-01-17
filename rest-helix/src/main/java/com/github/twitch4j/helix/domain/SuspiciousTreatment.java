package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SuspiciousTreatment {

    /**
     * Messages sent by monitored accounts will appear in chat,
     * but flagged in the chat panel and Mod View widget for moderators and streamers to review.
     */
    @JsonProperty("ACTIVE_MONITORING")
    MONITOR,

    /**
     * Messages sent by restricted accounts will be displayed only to the streamer and their moderators.
     */
    @JsonProperty("RESTRICTED")
    RESTRICT

}
