package me.philippheuer.twitch4j.enums;

/**
 * When requesting authorization from users, the scope parameter allows you to specify
 * which permissions your app requires. These scopes are ties to the access token you
 * receive upon a successful authorization. Without specifying scopes, your app only has
 * access to basic information about the authenticated user. You may specify any or all
 * of the following scopes.
 *
 * @author Urgue - Github [https://raw.githubusercontent.com/urgrue/Java-Twitch-Api-Wrapper/master/src/main/java/com/mb3364/twitch/api/auth/Scopes.java]
 */
public enum TwitchScopes {
	/**
	 * Read access to non-public user information, such as email address.
	 */
	USER_READ("user_read"),

	/**
	 * Ability to ignore or unignore on behalf of a user.
	 */
	USER_BLOCKS_EDIT("user_blocks_edit"),

	/**
	 * Read access to a user's list of ignored users.
	 */
	USER_BLOCKS_READ("user_blocks_read"),

	/**
	 * Access to manage a user's followed channels.
	 */
	USER_FOLLOWS_EDIT("user_follows_edit"),

	/**
	 * Read access to non-public channel information, including email address and stream key.
	 */
	CHANNEL_READ("channel_read"),

	/**
	 * Write access to channel metadata (game, status, etc).
	 */
	CHANNEL_EDITOR("channel_editor"),

	/**
	 * Access to trigger commercials on channel.
	 */
	CHANNEL_COMMERCIAL("channel_commercial"),

	/**
	 * Ability to reset a channel's stream key.
	 */
	CHANNEL_STREAM("channel_stream"),

	/**
	 * Read access to all subscribers to your channel.
	 */
	CHANNEL_SUBSCRIPTIONS("channel_subscriptions"),

	/**
	 * Read access to subscriptions of a user.
	 */
	USER_SUBSCRIPTIONS("user_subscriptions"),

	/**
	 * Read and write access to the channel feed.
	 */
	CHANNEL_FEED_EDIT("channel_feed_edit"),

	/**
	 * Read access to check if a user is subscribed to your channel.
	 */
	CHANNEL_CHECK_SUBSCRIPTION("channel_check_subscription"),

	/**
	 * Ability to log into chat and send messages.
	 */
	CHAT_LOGIN("chat_login");

	/**
	 * Scope Key
	 */
	private String key;

	/**
	 * Class Constructor
	 *
	 * @param key
	 */
	TwitchScopes(String key) {
		this.key = key;
	}

	/**
	 * Combine <code>TwitchScopes</code> into a '+' separated <code>String</code>.
	 * This is the required input format for twitch.tv
	 *
	 * @param scopes <code>TwitchScopes</code> to combine.
	 * @return <code>String</code> representing '+' separated list of <code>TwitchScopes</code>
	 */
	public static String join(TwitchScopes... scopes) {
		if (scopes == null) return "";
		StringBuilder sb = new StringBuilder();
		for (TwitchScopes scope : scopes) {
			sb.append(scope.getKey()).append("+");
		}
		if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '+') {
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * Gets the enum from the string representation of the scope.
	 *
	 * @param text Text representation of Enum value
	 * @return Enum value that the text represents
	 */
	public static TwitchScopes fromString(String text) {
		if (text == null) {
			return null;
		}

		for (TwitchScopes b : TwitchScopes.values()) {
			if (text.equalsIgnoreCase(b.key)) {
				return b;
			}
		}

		return null;
	}

	/**
	 * Get the identifier that oauth will recognize.
	 *
	 * @return A <code>String</code> identifier of the scope.
	 */
	public String getKey() {
		return key;
	}

	@Override
	public String toString() {
		return key;
	}
}
