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
public class HypeTrainEndEvent extends HypeTrainEvent {

    /**
     * The timestamp at which the hype train ended.
     */
    private Instant endedAt;

    /**
     * The timestamp at which the hype train cooldown ends so that the next hype train can start.
     */
    private Instant cooldownEndsAt;

}
