package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum AutomodCaughtReason {

    /**
     * The message was caught by an AutoMod category.
     */
    AUTOMOD,

    /**
     * The message was caught for containing a blocked term.
     */
    BLOCKED_TERM,

    /**
     * The message contained a link and the channel has 'Hold Hyperlinks for Review' enabled.
     */
    BLOCKED_LINK,

    /**
     * The message was caught for an unknown reason; please report to our issue tracker.
     */
    @JsonEnumDefaultValue
    UNKNOWN

}
