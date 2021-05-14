package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum PollStatus {

    /**
     * Poll is currently in progress.
     */
    ACTIVE,

    /**
     * Poll has reached its ended_at time.
     */
    COMPLETED,

    /**
     * Poll has been manually terminated before its ended_at time, but still visible.
     */
    TERMINATED,

    /**
     * Poll is no longer visible on the channel.
     */
    ARCHIVED,

    /**
     * Poll is no longer visible to any user on Twitch.
     */
    MODERATED,

    /**
     * Something went wrong determining the state.
     */
    @JsonEnumDefaultValue
    INVALID

}
