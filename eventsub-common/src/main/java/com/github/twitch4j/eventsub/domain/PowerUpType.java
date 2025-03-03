package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum PowerUpType {

    /**
     * Message Effects was redeemed.
     */
    MESSAGE_EFFECT,

    /**
     * On-Screen Celebration was redeemed.
     */
    CELEBRATION,

    /**
     * Gigantify an Emote was redeemed.
     */
    GIGANTIFY_AN_EMOTE,

    /**
     * An unrecognized Power-up type; please report to our issue tracker.
     */
    @JsonEnumDefaultValue
    UNKNOWN

}
