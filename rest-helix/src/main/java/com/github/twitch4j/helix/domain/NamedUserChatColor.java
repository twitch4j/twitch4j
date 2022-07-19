package com.github.twitch4j.helix.domain;

/**
 * The color to use for the user’s name in chat.
 * <p>
 * All users may specify one of the following named color values.
 * <p>
 * Turbo and Prime users may specify a named color or a Hex color code like #9146FF.
 * If you use a Hex color code, remember to URL encode it.
 *
 * @see com.github.twitch4j.helix.TwitchHelix#updateUserChatColor(String, String, String)
 */
public enum NamedUserChatColor {
    BLUE,
    BLUE_VIOLET,
    CADET_BLUE,
    CHOCOLATE,
    CORAL,
    DODGER_BLUE,
    FIREBRICK,
    GOLDEN_ROD,
    GREEN,
    HOT_PINK,
    ORANGE_RED,
    RED,
    SEA_GREEN,
    SPRING_GREEN,
    YELLOW_GREEN;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
