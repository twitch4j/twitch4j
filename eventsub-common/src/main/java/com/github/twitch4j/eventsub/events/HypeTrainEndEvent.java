package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
public class HypeTrainEndEvent extends HypeTrainEvent {

    /**
     * Current level of hype train event.
     */
    private Integer level;

    /**
     * The timestamp at which the hype train ended.
     */
    private Instant endedAt;

    /**
     * The timestamp at which the hype train cooldown ends so that the next hype train can start.
     */
    private Instant cooldownEndsAt;

}
