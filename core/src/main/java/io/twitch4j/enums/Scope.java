package io.twitch4j.enums;

import io.vertx.core.json.JsonArray;

import java.util.Arrays;
import java.util.Collection;

public enum Scope {
    /**
     * Manage a clip object.
     */
    CLIPS_EDIT("clips:edit"),
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
    VIEWING_ACTIVITY_READ,
    /**
     * Empty Scope
     */
    NONE;

    private final String name;

    Scope(String scope) {
        this.name = scope;
    }

    Scope() {
        this.name = name().toLowerCase();
    }

    public static boolean hasScope(Collection<Scope> scopes, Scope scope) {
        return scopes.contains(scope);
    }

    public static boolean hasScopes(Collection<Scope> scopes, Scope... scope) {
        return hasScopes(scopes, Arrays.asList(scope));
    }

    private static <T> boolean hasScopes(Collection<Scope> scopes, Collection<Scope> scope) {
        return scopes.containsAll(scope);
    }

    public static JsonArray parseList(String scope) {
        return new JsonArray(Arrays.asList(scope.split("(/+|/s)")));
    }

    @Override
    public String toString() {
        return this.name;
    }
}
