package me.philippheuer.twitch4j.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * When requesting authorization from users, the scope parameter allows you to specify
 * which permissions your app requires. These scopes are ties to the access token you
 * receive upon a successful authorization. Without specifying scopes, your app only has
 * access to basic information about the authenticated user. You may specify any or all
 * of the following scopes.
 *
 * @author Urgue - Github [https://raw.githubusercontent.com/urgrue/Java-Twitch-Api-Wrapper/master/src/main/java/com/mb3364/twitch/api/auth/Scopes.java]
 * @author Damian Staszewski
 * @version %I%, %G%
 * @since 1.0
 */
@Getter
public enum Scope {
    /**
     * Manage a user object.
     */
    USER_EDIT("user:edit"),
    /**
     * Read authorized user's email address.
     */
    USER_READ_EMAIL("user:read:email"),
    /**
     * Read whether a user is subscribed to your channel.
     */
    CHANNEL_CHECK_SUBSCRIPTION,
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
     * Read nonpublic channel information, including email address and String key.
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
     * Manage a user's collections (of videos).
     */
    COLLECTIONS_EDIT,
    /**
     * Manage a user's communities.
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
     * Turn on/off ignoring a user. Ignoring a user means you cannot see him type, receive messages from him, etc.
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
     * Scope Key
     */
    private final String key;

    /**
     * Class Constructor
     *
     * @param key
     */
    Scope(String key) {
        this.key = key;
    }

    Scope() { this.key = name().toLowerCase(); }

    /**
     * Combine <code>Scope</code> into a '+' separated <code>String</code>.
     * This is the required input format for twitch.tv
     *
     * @param scopes <code>Scope</code> to combine.
     * @return <code>String</code> representing '+' separated list of <code>Scope</code>
     */
    public static String join(Scope... scopes) {
        if (scopes == null) return "";
        return join(Arrays.asList(scopes));
    }

    public static String join(Collection<Scope> scopes) {
        if (scopes.size() == 0) return "";
        return scopes.stream().map(Scope::getKey).collect(Collectors.joining("+"));
    }

    @Override
    public String toString() {
        return key;
    }
}
