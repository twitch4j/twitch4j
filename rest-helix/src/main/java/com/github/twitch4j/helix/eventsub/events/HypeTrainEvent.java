package com.github.twitch4j.helix.eventsub.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.twitch4j.helix.eventsub.domain.Contribution;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class HypeTrainEvent extends EventSubChannelEvent {

    /**
     * Total points contributed to hype train.
     */
    private Integer total;

    /**
     * The number of points required to reach the next level.
     */
    private Integer goal;

    /**
     * The contributors with the most points contributed.
     */
    private Contribution topContributions;

    /**
     * The most recent contribution.
     */
    private Contribution lastContribution;

    /**
     * The timestamp at which the hype train started.
     */
    private Instant startedAt;

    /**
     * The time at which the hype train expires.
     * The expiration is extended when the hype train reaches a new level.
     */
    private Instant expiresAt;

}
