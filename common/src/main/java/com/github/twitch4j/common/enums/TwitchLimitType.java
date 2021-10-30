package com.github.twitch4j.common.enums;

/**
 * The types of rate-limits imposed by Twitch.
 */
public enum TwitchLimitType {

    /**
     * How fast authentication attempts can be issued over IRC.
     * <p>
     * Note: this limit is <i>not</i> currently implemented elsewhere in the library.
     */
    CHAT_AUTH_LIMIT,

    /**
     * How fast channel join attempts can be issued over IRC.
     */
    CHAT_JOIN_LIMIT,

    /**
     * How fast messages can be issued over IRC.
     */
    CHAT_MESSAGE_LIMIT,

    /**
     * How fast private messages can be issued over IRC.
     */
    CHAT_WHISPER_LIMIT

}
