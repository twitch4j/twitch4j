package com.github.twitch4j.chat.enums;

/**
 * Dictates the handling of Shared Chat messages.
 */
public enum MirroredMessagePolicy {

    /**
     * Always publish mirrored chat events.
     * When using this policy, you should handle Shared Chat logic on your own.
     */
    ACCEPT_ALL,

    /**
     * Publish mirrored chat events if the message has not already been observed (and the source channel is not joined).
     * This policy avoids firing effectively duplicated events.
     * However, if your bot is distributed over multiple servers, you should utilize {@link #ACCEPT_ALL}
     * and handle de-duplication yourself.
     */
    REJECT_IF_OBSERVED,

    /**
     * Never publish mirrored chat events.
     * This is the most conservative option, which avoids any risk of duplicated events.
     * However, this policy can be undesirable for moderation bots as it can enable bypassing of the channel rules.
     */
    REJECT_ALL

}

