package twitch4j.chat.commands;

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
