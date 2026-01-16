package com.github.twitch4j.eventsub.events;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HypeTrainBeginV2Event extends HypeTrainV2Event {

    /**
     * The number of points contributed to the hype train at the current level.
     */
    private Integer progress;

    /**
     * The number of points required to reach the next level.
     */
    private Integer goal;

    /**
     * The time at which the hype train expires.
     * The expiration is extended when the hype train reaches a new level.
     */
    private Instant expiresAt;

    /**
     * The all-time high level this type of Hype Train has reached for this broadcaster.
     */
    private int allTimeHighLevel;

    /**
     * The all-time high total this type of Hype Train has reached for this broadcaster.
     */
    private int allTimeHighTotal;

}
