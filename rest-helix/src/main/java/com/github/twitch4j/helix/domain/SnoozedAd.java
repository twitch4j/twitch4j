package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class SnoozedAd {

    /**
     * The number of snoozes available for the broadcaster.
     */
    private Integer snoozeCount;

    /**
     * The UTC timestamp when the broadcaster will gain an additional snooze, in RFC3339 format.
     */
    private Instant snoozeRefreshAt;

    /**
     * The UTC timestamp of the broadcaster’s next scheduled ad, in RFC3339 format.
     */
    private Instant nextAdAt;

}
