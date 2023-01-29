package com.github.twitch4j.eventsub.events;

public interface ModeratorEvent {

    /**
     * The user ID of the issuer of the moderation action.
     */
    String getModeratorUserId();

    /**
     * The user login of the issuer of the moderation action.
     */
    String getModeratorUserLogin();

    /**
     * The user name of the issuer of the moderation action.
     */
    String getModeratorUserName();

}
