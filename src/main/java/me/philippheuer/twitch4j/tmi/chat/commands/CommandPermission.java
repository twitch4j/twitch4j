package me.philippheuer.twitch4j.tmi.chat.commands;

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
