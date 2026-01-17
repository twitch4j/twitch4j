package com.github.twitch4j.helix.domain;

import com.github.twitch4j.eventsub.domain.SuspiciousStatus;
import com.github.twitch4j.eventsub.domain.SuspiciousType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;
import java.util.EnumSet;

@Data
@Setter(AccessLevel.PRIVATE)
public class SuspiciousUser {

    /**
     * The ID of the user having their suspicious status updated.
     */
    private String userId;

    /**
     * The user ID of the broadcaster indicating in which channel the status is being applied.
     */
    private String broadcasterId;

    /**
     * The user ID of the moderator who applied the last status.
     */
    private String moderatorId;

    /**
     * The timestamp of the last time this user's status was updated.
     */
    private Instant updatedAt;

    /**
     * The type of suspicious status.
     */
    private SuspiciousStatus status;

    /**
     * The type(s) of suspicious user this is.
     */
    private EnumSet<SuspiciousType> types;

}
