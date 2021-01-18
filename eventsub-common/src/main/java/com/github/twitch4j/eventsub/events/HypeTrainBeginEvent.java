package com.github.twitch4j.eventsub.events;

import com.github.twitch4j.eventsub.domain.Contribution;
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
public class HypeTrainBeginEvent extends HypeTrainEvent {

    /**
     * The number of points contributed to the hype train at the current level.
     */
    private Integer progress;

    /**
     * The number of points required to reach the next level.
     */
    private Integer goal;

    /**
     * The most recent contribution.
     */
    private Contribution lastContribution;

    /**
     * The time at which the hype train expires.
     * The expiration is extended when the hype train reaches a new level.
     */
    private Instant expiresAt;

}
