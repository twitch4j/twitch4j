package com.github.twitch4j.eventsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class BlockedTerm {

    /**
     * The id of the blocked term found.
     */
    private String termId;

    /**
     * The bounds of the text that caused the message to be caught.
     */
    private Boundary boundary;

    /**
     * The id of the broadcaster that owns the blocked term.
     */
    private String ownerBroadcasterUserId;

    /**
     * The login of the broadcaster that owns the blocked term.
     */
    private String ownerBroadcasterUserLogin;

    /**
     * The username of the broadcaster that owns the blocked term.
     */
    private String ownerBroadcasterUserName;

}
