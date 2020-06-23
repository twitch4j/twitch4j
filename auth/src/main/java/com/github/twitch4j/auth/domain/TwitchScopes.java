package com.github.twitch4j.auth.domain;

public enum TwitchScopes {

    /**
     * Scopes
     */
    CHAT_CHANNEL_MODERATE("channel:moderate"),
    CHAT_EDIT("chat:edit"),
    CHAT_READ("chat:read"),
    CHAT_WHISPERS_READ("whispers:read"),
    CHAT_WHISPERS_EDIT("whispers:edit"),
    HELIX_ANALYTICS_READ_EXTENSIONS("analytics:read:extensions"),
    HELIX_ANALYTICS_READ_GAMES("analytics:read:games"),
    HELIX_BITS_READ("bits:read"),
    HELIX_CLIPS_EDIT("clips:edit"),
    HELIX_CHANNEL_COMMERCIALS_EDIT("channel:edit:commercial"),
    HELIX_CHANNEL_HYPE_TRAIN_READ("channel:read:hype_train"),
    HELIX_CHANNEL_REDEMPTIONS_READ("channel:read:redemptions"),
    HELIX_CHANNEL_STREAM_KEY_READ("channel:read:stream_key"),
    HELIX_CHANNEL_SUBSCRIPTIONS_READ("channel:read:subscriptions"),
    HELIX_MODERATION_READ("moderation:read"),
    HELIX_USER_EDIT("user:edit"),
    HELIX_USER_EDIT_BROADCAST("user:edit:broadcast"),
    HELIX_USER_EDIT_FOLLOWS("user:edit:follows"),
    HELIX_USER_READ_BROADCAST("user:read:broadcast"),
    HELIX_USER_EMAIL("user:read:email"),
    KRAKEN_CHANNEL_CHECK_SUBSCRIPTION("channel_check_subscription"),
    KRAKEN_CHANNEL_COMMERCIAL("channel_commercial"),
    KRAKEN_CHANNEL_EDITOR("channel_editor"),
    KRAKEN_CHANNEL_FEED_EDIT("channel_feed_edit"),
    KRAKEN_CHANNEL_FEED_READ("channel_feed_read"),
    KRAKEN_CHANNEL_READ("channel_read"),
    KRAKEN_CHANNEL_STREAM("channel_stream"),
    KRAKEN_CHANNEL_SUBSCRIPTIONS("channel_subscriptions"),
    KRAKEN_COLLECTIONS_EDIT("collections_edit"),
    KRAKEN_COMMUNITIES_EDIT("communities_edit"),
    KRAKEN_COMMUNITIES_MODERATE("communities_moderate"),
    KRAKEN_OPENID("openid"),
    KRAKEN_USER_BLOCKS_EDIT("user_blocks_edit"),
    KRAKEN_USER_BLOCKS_READ("user_blocks_read"),
    KRAKEN_USER_FOLLOWS_EDIT("user_follows_edit"),
    KRAKEN_USER_READ("user_read"),
    KRAKEN_USER_SUBSCRIPTIONS("user_subscriptions"),
    KRAKEN_VIEWING_ACTIVITY_READ("viewing_activity_read"),
    @Deprecated KRAKEN_CHAT_LOGIN("chat_login"),
    @Deprecated HIDDEN_USER_PUSH_SUBSCRIPTIONS_EDIT("user_push_subscriptions_edit"),
    @Deprecated HIDDEN_CHANNEL_FEED_REPORT("channel_feed_report"),
    @Deprecated HIDDEN_USER_EDIT("user_edit"),
    @Deprecated HIDDEN_USER_FRIENDS_EDIT("user_friends_edit"),
    @Deprecated HIDDEN_USER_FRIENDS_READ("user_friends_read"),
    @Deprecated HIDDEN_USER_PRESENCE_EDIT("user_presence_edit"),
    @Deprecated HIDDEN_USER_PRESENCE_FRIENDS_READ("user_presence_friends_read"),
    @Deprecated HIDDEN_USER_SUBSCRIPTIONS_EDIT("user_subscriptions_edit"),
    @Deprecated HIDDEN_USER_ENTITLEMENTS_READ("user_entitlements_read");

    /**
     * Scope Text
     */
    private final String name;

    /**
     * Constructor
     *
     * @param name string value
     */
    TwitchScopes(String name) {
        this.name = name;
    }

    /**
     * Custom ToString
     *
     * @return string
     */
    public String toString() {
        return this.name;
    }
}
