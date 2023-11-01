package com.github.twitch4j.common.enums;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum AnnouncementColor {

    @JsonEnumDefaultValue
    PRIMARY,
    BLUE,
    GREEN,
    ORANGE,
    PURPLE;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
