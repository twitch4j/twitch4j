package com.github.twitch4j.helix.domain;

public enum UnbanRequestStatus {

    /**
     * An unban has been requested, but not yet resolved by a moderator.
     */
    PENDING,

    /**
     * A moderator approved the unban request.
     */
    APPROVED,

    /**
     * A moderator denied the unban request.
     */
    DENIED,

    /**
     * The unban request was approved and the user acknowledged the resolution text.
     */
    ACKNOWLEDGED,

    /**
     * The user withdrew their request for an unban.
     */
    CANCELED;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

}
