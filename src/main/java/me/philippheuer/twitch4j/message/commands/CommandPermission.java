package me.philippheuer.twitch4j.message.commands;

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
