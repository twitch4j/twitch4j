package com.github.twitch4j.internal;

/**
 * The primary key that is associated with the rate limit bucket for this (helix) command.
 */
enum ChatCommandRateLimitType {

    /**
     * The command should be rate limited based on which channel it was executed in.
     */
    CHANNEL,

    /**
     * The command should be rate limited based on which user executed it.
     * <p>
     * As {@code token} is final, this assumed to be a single user.
     */
    USER

}
