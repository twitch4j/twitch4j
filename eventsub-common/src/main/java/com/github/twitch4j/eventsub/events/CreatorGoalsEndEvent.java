package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CreatorGoalsEndEvent extends CreatorGoalsEvent {

    /**
     * A Boolean value that indicates whether the broadcaster achieved their goal.
     * Is true if the goal was achieved; otherwise, false.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_achieved")
    private Boolean isAchieved;

    /**
     * The UTC timestamp in RFC 3339 format, which indicates when the broadcaster ended the goal.
     */
    private Instant endedAt;

}
