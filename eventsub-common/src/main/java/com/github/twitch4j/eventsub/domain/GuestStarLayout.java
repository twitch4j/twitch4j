package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * How the guests within a session should be laid out within a group browser source.
 */
public enum GuestStarLayout {

    /**
     * All live guests are tiled within the browser source with the same size.
     */
    @JsonAlias("tiled")
    TILED_LAYOUT,

    /**
     * All live guests are tiled within the browser source with the same size.
     * If there is an active screen share, it is sized larger than the other guests.
     */
    @JsonAlias("screenshare")
    SCREENSHARE_LAYOUT

}
