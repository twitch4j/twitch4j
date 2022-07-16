package com.github.twitch4j.helix.domain;

/**
 * The color used to highlight the announcement.
 */
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
}
