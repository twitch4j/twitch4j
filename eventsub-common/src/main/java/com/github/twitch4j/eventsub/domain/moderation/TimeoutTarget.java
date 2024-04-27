package com.github.twitch4j.eventsub.domain.moderation;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TimeoutTarget extends UserTarget {

    /**
     * The optional reason given for the timeout.
     */
    @Nullable
    private String reason;

    /**
     * The time at which the timeout ends.
     */
    private Instant expiresAt;

}
