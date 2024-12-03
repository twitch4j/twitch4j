package com.github.twitch4j.eventsub.domain;

public enum AutomodCaughtReason {

    /**
     * The message was caught by an AutoMod category.
     */
    AUTOMOD,

    /**
     * The message was caught for containing a blocked term.
     */
    BLOCKED_TERM

}
