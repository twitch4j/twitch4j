package com.github.twitch4j.helix.domain;

/**
 * The color used to highlight the announcement.
 *
 * @deprecated in favor of {@link com.github.twitch4j.common.enums.AnnouncementColor}
 */
@Deprecated
public enum AnnouncementColor {
    BLUE,
    GREEN,
    ORANGE,
    PURPLE,
    PRIMARY;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    public com.github.twitch4j.common.enums.AnnouncementColor converted() {
        switch (this) {
            case BLUE:
                return com.github.twitch4j.common.enums.AnnouncementColor.BLUE;
            case GREEN:
                return com.github.twitch4j.common.enums.AnnouncementColor.GREEN;
            case ORANGE:
                return com.github.twitch4j.common.enums.AnnouncementColor.ORANGE;
            case PURPLE:
                return com.github.twitch4j.common.enums.AnnouncementColor.PURPLE;
            default:
                return com.github.twitch4j.common.enums.AnnouncementColor.PRIMARY;
        }
    }
}
