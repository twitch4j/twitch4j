package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Flags that caused a user to be marked as suspicious.
 */
public enum SuspiciousType {

    /**
     * A moderator manually marked the user as restricted or monitored.
     */
    MANUALLY_ADDED,

    /**
     * Twitch suspects the user is ban evading.
     *
     * @see <a href="https://help.twitch.tv/s/article/suspicious-user-controls?language=en_US#what">Official Help Article</a>
     */
    @JsonProperty("ban_evader")
    DETECTED_BAN_EVADER,

    /**
     * User was banned in another channel that has a ban sharing relationship with the current channel.
     *
     * @see <a href="https://help.twitch.tv/s/article/suspicious-user-controls?language=en_US#shared">Official Help Article</a>
     */
    BANNED_IN_SHARED_CHANNEL,

    /**
     * User was marked as suspicious for an unknown reason; please report to our issue tracker.
     */
    @JsonEnumDefaultValue
    UNKNOWN

}
