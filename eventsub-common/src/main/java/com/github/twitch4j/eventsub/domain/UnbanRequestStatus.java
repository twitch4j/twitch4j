package com.github.twitch4j.eventsub.domain;

/**
 * @see com.github.twitch4j.eventsub.events.UnbanRequestResolvedEvent
 */
public enum UnbanRequestStatus {

    /**
     * A moderator approved the unban request.
     */
    APPROVED,

    /**
     * The user withdrew their request for an unban.
     */
    CANCELED,

    /**
     * A moderator denied the unban request.
     */
    DENIED

}
