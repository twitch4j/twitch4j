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
    HELIX_USER_EDIT("user:edit"),
    HELIX_USER_EDIT_BROADCAST("user:edit:broadcast"),
    HELIX_USER_READ_BROADCAST("user:read:broadcast"),
    HELIX_USER_EMAIL("user:read:email");

    /**
     * Scope Text
     */
    private final String name;

    /**
     * Constructor
     *
     * @param name string value
     */
    private TwitchScopes(String name) {
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
