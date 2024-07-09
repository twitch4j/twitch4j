package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum AutomodMessageStatus {

    /**
     * A moderator allowed this message flagged by AutoMod to be sent in chat.
     */
    APPROVED,

    /**
     * A moderator denied this message flagged by AutoMod from being sent in chat.
     */
    DENIED,

    /**
     * This message caught by AutoMod was not approved or denied by a moderator in time, so it was not sent in chat.
     */
    EXPIRED,

    /**
     * An invalid message status; please report to our issue tracker.
     */
    @JsonEnumDefaultValue
    INVALID

}
