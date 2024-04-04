package com.github.twitch4j.eventsub.domain;

/**
 * The status change applied to the terms.
 *
 * @see com.github.twitch4j.eventsub.events.ChannelTermsUpdateEvent
 */
public enum TermUpdateType {
    ADD_PERMITTED,
    REMOVE_PERMITTED,
    ADD_BLOCKED,
    REMOVE_BLOCKED;

    /**
     * @return whether the update is the addition of a new term. otherwise, a term was removed.
     */
    public boolean isAddition() {
        return this == ADD_PERMITTED || this == ADD_BLOCKED;
    }
}
