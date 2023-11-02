package com.github.twitch4j.chat.enums;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.github.twitch4j.common.annotation.Unofficial;

/**
 * @deprecated in favor of {@link com.github.twitch4j.common.enums.AnnouncementColor}
 */
@Deprecated
@Unofficial
public enum AnnouncementColor {

    @JsonEnumDefaultValue
    PRIMARY,
    BLUE,
    GREEN,
    ORANGE,
    PURPLE;

    private static final AnnouncementColor[] COLORS = values();

    @Unofficial
    public static AnnouncementColor parseColor(String colorStr) {
        for (AnnouncementColor color : COLORS) {
            if (color.toString().equalsIgnoreCase(colorStr))
                return color;
        }
        return PRIMARY;
    }

    public static AnnouncementColor from(com.github.twitch4j.common.enums.AnnouncementColor commonColor) {
        switch (commonColor) {
            case BLUE:
                return BLUE;
            case GREEN:
                return GREEN;
            case ORANGE:
                return ORANGE;
            case PURPLE:
                return PURPLE;
            default:
                return PRIMARY;
        }
    }

}
