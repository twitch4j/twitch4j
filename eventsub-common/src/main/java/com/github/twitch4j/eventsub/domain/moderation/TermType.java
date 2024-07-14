package com.github.twitch4j.eventsub.domain.moderation;

/**
 * @see <a href="https://help.twitch.tv/s/article/how-to-use-automod?language=en_US#BlockedandPermitted">Help article for AutoMod term lists</a>
 */
public enum TermType {

    /**
     * The AutoMod term belongs to the blocked terms list.
     */
    BLOCKED,

    /**
     * The AutoMod term belongs to the permitted terms list..
     */
    PERMITTED

}
