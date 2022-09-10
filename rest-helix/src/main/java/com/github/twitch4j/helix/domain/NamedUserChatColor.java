package com.github.twitch4j.helix.domain;

import com.github.twitch4j.common.annotation.Unofficial;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
public enum NamedUserChatColor {
    BLUE("#0000FF"),
    BLUE_VIOLET("#8A2BE2"),
    CADET_BLUE("#5F9EA0"),
    CHOCOLATE("#D2691E"),
    CORAL("#FF7F50"),
    DODGER_BLUE("#1E90FF"),
    FIREBRICK("#B22222"),
    GOLDEN_ROD("#DAA520"),
    GREEN("#008000"),
    HOT_PINK("#FF69B4"),
    ORANGE_RED("#FF4500"),
    RED("#FF0000"),
    SEA_GREEN("#2E8B57"),
    SPRING_GREEN("#00FF7F"),
    YELLOW_GREEN("#9ACD32");

    @Getter(onMethod_ = { @Unofficial })
    private final String hexCode;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
