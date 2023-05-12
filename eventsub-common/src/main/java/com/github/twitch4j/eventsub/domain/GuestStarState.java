package com.github.twitch4j.eventsub.domain;

/**
 * The current state of the user after the update has taken place.
 */
public enum GuestStarState {

    /**
     * The guest has transitioned to the invite queue.
     * <p>
     * This can take place when the guest was previously assigned a slot,
     * but have been removed from the call and are sent back to the invite queue.
     */
    INVITED,

    /**
     * The guest has signaled they are ready and can be assigned a slot.
     */
    READY,

    /**
     * The guest has been assigned a slot in the session, but is not currently seen live in the broadcasting software.
     */
    BACKSTAGE,

    /**
     * The guest is now live in the host’s broadcasting software.
     */
    LIVE,

    /**
     * The guest was removed from the call or queue.
     */
    REMOVED

}
