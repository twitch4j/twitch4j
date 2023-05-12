package com.github.twitch4j.eventsub.domain;

/**
 * How the guests within a session should be laid out within a group browser source.
 */
public enum GuestStarLayout {

    /**
     * All live guests are tiled within the browser source with the same size.
     */
    TILED,

    /**
     * All live guests are tiled within the browser source with the same size.
     * If there is an active screen share, it is sized larger than the other guests.
     */
    SCREENSHARE

}
