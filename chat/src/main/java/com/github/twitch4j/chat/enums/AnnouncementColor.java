package com.github.twitch4j.chat.enums;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.github.twitch4j.common.annotation.Unofficial;

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

}
