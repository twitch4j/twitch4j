package com.github.twitch4j.common.enums;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.github.twitch4j.util.EnumUtil;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public enum AnnouncementColor {

    @JsonEnumDefaultValue
    PRIMARY,
    BLUE,
    GREEN,
    ORANGE,
    PURPLE;

    private static final Map<String, AnnouncementColor> MAPPINGS = EnumUtil.buildMapping(AnnouncementColor.values());

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    @NotNull
    @ApiStatus.Internal
    public static AnnouncementColor parseColor(@NotNull String colorStr) {
        return MAPPINGS.getOrDefault(colorStr.toLowerCase(), PRIMARY);
    }
}
