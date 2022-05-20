package com.github.twitch4j.common.enums;

public enum CommandPermission {
    /**
     * Everyone
     */
    EVERYONE,

    /**
     * Twitch Prime/Twitch Turbo User
     */
    PRIME_TURBO,

    /**
     * Twitch Partners
     */
    PARTNER,

    /**
     * Subscriber (any Tier)
     */
    SUBSCRIBER,

    /**
     * Founder (first 10 subscribers of an Affiliate, or first 25 subscribers of a Partner)
     */
    FOUNDER,

    /**
     * Gifted a sub
     */
    SUBGIFTER,

    /**
     * Cheered bits
     */
    BITS_CHEERER,

    /**
     * Was a conductor of the previous hype train
     */
    FORMER_HYPE_TRAIN_CONDUCTOR,

    /**
     * A conductor of the latest hype train
     */
    CURRENT_HYPE_TRAIN_CONDUCTOR,

    /**
     * Participated in the most recent predictions event for a blue option
     * <p>
     * Warning: when there are three or more prediction choices, they are all blue
     */
    PREDICTIONS_BLUE,

    /**
     * Participated in the most recent predictions event for a pink option
     */
    PREDICTIONS_PINK,

    /**
     * Watching without audio (user-selected)
     */
    NO_AUDIO,

    /**
     * Watching without video (user-selected)
     */
    NO_VIDEO,

    /**
     * Earned for being a part of at least 1 moment on a channel
     */
    MOMENTS,

    /**
     * Artist on this Channel
     */
    ARTIST,

    /**
     * VIP
     */
    VIP,

    /**
     * Twitch Staff
     */
    TWITCHSTAFF,

    /**
     * Channel Moderator
     */
    MODERATOR,

    /**
     * Broadcaster
     */
    BROADCASTER,

    /**
     * Bot Owner
     */
    OWNER
}
