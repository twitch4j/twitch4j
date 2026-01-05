package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class HypeTrainMilestone {

    /**
     * The level of the record Hype Train.
     */
    private int level;

    /**
     * Total points contributed to the record Hype Train.
     */
    private int total;

    /**
     * The time when the record was achieved.
     */
    private Instant achievedAt;

}
