package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

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
     * Channel-specific power-ups.
     *
     * @see <a href="https://blog.twitch.tv/en/2026/05/19/new-ways-to-turn-your-community-s-participation-into-earnings/">Marketing Article</a>
     * @see <a href="https://help.twitch.tv/s/article/power-ups">Official Help Article</a>
     */
    CUSTOM_POWER_UP,

    /**
     * An experimental form of bits that triggers animations upon combo levels being reached.
     *
     * @see <a href="https://help.twitch.tv/s/article/combos?language=en_US">Twitch Help Article</a>
     * @deprecated Twitch removed the combos experiment in late March 2026.
     */
    @Deprecated
    COMBO,

    /**
     * An unrecognized bits usage type; please report to our issue tracker.
     */
    @JsonEnumDefaultValue
    UNKNOWN

}
