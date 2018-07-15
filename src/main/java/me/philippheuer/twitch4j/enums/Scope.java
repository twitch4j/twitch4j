package me.philippheuer.twitch4j.enums;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

/**
 * When requesting authorization from users, the scope parameter allows you to specify
 * which permissions your app requires. These scopes are ties to the access token you
 * receive upon a successful authorization. Without specifying scopes, your app only has
 * access to basic information about the authenticated user. You may specify any or all
 * of the following scopes.
 *
 * @author Urgue - Github [https://raw.githubusercontent.com/urgrue/Java-Twitch-Api-Wrapper/master/src/main/java/com/mb3364/twitch/api/auth/Scopes.java]
 */
@RequiredArgsConstructor
public enum Scope {
	/**
	 * View analytics data for your games.
	 */
	ANALYTICS_READ_GAMES("analytics:read:games"),
	/**
	 * View bits information for your channel.
	 */
	BITS_READ("bits:read"),
	/**
	 * Manage a clip object.
	 */
	CLIPS_EDIT("clips:edit"),
	/**
	 * Manage a user object.
	 */
	USER_EDIT("user:edit"),
	/**
	 * Read authorized user’s email address.
	 */
	USER_READ_EMAIL("user:read:email"),

	/**
	 * Read whether a user is subscribed to your channel.
	 */
	channel_check_subscription,
	/**
	 * Trigger commercials on channel.
	 */
	CHANNEL_COMMERCIAL,
	/**
	 * Write channel metadata (game, status, etc).
	 */
	CHANNEL_EDITOR,
	/**
	 * Add posts and reactions to a channel feed.
	 */
	CHANNEL_FEED_EDIT,
	/**
	 * View a channel feed.
	 */
	CHANNEL_FEED_READ,
	/**
	 * Read nonpublic channel information, including email address and stream key.
	 */
	CHANNEL_READ,
	/**
	 * Reset a channel’s stream key.
	 */
	CHANNEL_STREAM,
	/**
	 * Read all subscribers to your channel.
	 */
	CHANNEL_SUBSCRIPTIONS,
	/**
	 * Log into chat and send messages.
	 */
	CHAT_LOGIN,
	/**
	 * Manage a user’s collections (of videos).
	 */
	COLLECTIONS_EDIT,
	/**
	 * Manage a user’s communities.
	 */
	COMMUNITIES_EDIT,
	/**
	 * Manage community moderators.
	 */
	COMMUNITIES_MODERATE,
	/**
	 * Use OpenID Connect authentication.
	 */
	OPENID,
	/**
	 * Turn on/off ignoring a user. Ignoring users means you cannot see them type, receive messages from them, etc.
	 */
	USER_BLOCKS_EDIT,
	/**
	 * Read a user’s list of ignored users.
	 */
	USER_BLOCKS_READ,
	/**
	 * Manage a user’s followed channels.
	 */
	USER_FOLLOWS_EDIT,
	/**
	 * Read nonpublic user information, like email address.
	 */
	USER_READ,
	/**
	 * Read a user’s subscriptions.
	 */
	USER_SUBSCRIPTIONS,
	/**
	 * Turn on Viewer Heartbeat Service ability to record user data.
	 */
	VIEWING_ACTIVITY_READ;

	/**
	 * Get the identifier that oauth will recognize.
	 *
	 * @return A <code>{@link String}</code> identifier of the scope.
	 */
	private final String name;

	Scope() {
		this.name = name();
	}

	/**
	 * Combine <code>Scope</code> into a '+' separated <code>{@link String}</code>.
	 * This is the required input format for twitch.tv
	 *
	 * @param scopes <code>{@link Scope}</code> to combine.
	 * @return <code>{@link String}</code> representing '+' separated list of <code>{@link Scope}</code>
	 */
	public static String join(Scope... scopes) {
		return join(Arrays.asList(scopes));
	}

	public static String join(Collection<Scope> scopes) {
		if (scopes.size() > 0) return scopes.stream().map(Scope::name)
				.collect(Collectors.joining("+"));
		else return "";
	}

	/**
	 * Gets the enum from the string representation of the scope.
	 *
	 * @param text Text representation of Enum value
	 * @return Enum value that the text represents
	 */
	public static Scope fromString(String text) {
		if (text == null) {
			return null;
		}

		for (Scope b : Scope.values()) {
			if (text.equalsIgnoreCase(b.name)) {
				return b;
			}
		}

		return null;
	}


	@Override
	public String toString() {
		return name;
	}
}
