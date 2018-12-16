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
	 * Gifted a sub
	 */
	SUBGIFTER,

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
