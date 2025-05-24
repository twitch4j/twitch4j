package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import org.jetbrains.annotations.ApiStatus;

public enum BitsType {

    /**
     * A standard cheer with bits message.
     *
     * @see <a href="https://help.twitch.tv/s/article/guide-to-cheering-with-bits">Twitch Help Article</a>
     */
    CHEER,

    /**
     * A unique bits reward such as: "Gigantify an Emote," "Message Effects," and "On-Screen Celebration."
     *
     * @see <a href="https://help.twitch.tv/s/article/power-ups?language=en_US">Twitch Help Article</a>
     */
    POWER_UP,

    /**
     * An experimental form of bits that triggers animations upon combo levels being reached.
     *
     * @see <a href="https://help.twitch.tv/s/article/combos?language=en_US">Twitch Help Article</a>
     */
    @ApiStatus.Experimental
    COMBO,

    /**
     * An unrecognized bits usage type; please report to our issue tracker.
     */
    @JsonEnumDefaultValue
    UNKNOWN

}
