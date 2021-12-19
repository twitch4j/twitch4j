package com.github.twitch4j.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The types of rate-limits imposed by Twitch.
 */
@RequiredArgsConstructor
public enum TwitchLimitType {

    /**
     * How fast authentication attempts can be issued over IRC.
     */
    CHAT_AUTH_LIMIT("irc-auth-limit"),

    /**
     * How fast channel join attempts can be issued over IRC.
     */
    CHAT_JOIN_LIMIT("irc-join-limit"),

    /**
     * How fast messages can be issued over IRC.
     */
    CHAT_MESSAGE_LIMIT("irc-msg-limit"),

    /**
     * How fast private messages can be issued over IRC.
     */
    CHAT_WHISPER_LIMIT("irc-whisper-limit");

    /**
     * The identifier for the related bandwidth slot in a bucket for smoother replacement.
     */
    @Getter
    private final String bandwidthId;

}
