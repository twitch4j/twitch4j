package com.github.twitch4j.eventsub.events;

import com.github.twitch4j.eventsub.domain.PredictionStatus;
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
public class ChannelPredictionEndEvent extends ChannelPredictionEvent {

    /**
     * ID of the winning outcome.
     */
    private String winningOutcomeId;

    /**
     * The status of the Channel Points Prediction. Valid values are resolved and canceled.
     */
    private PredictionStatus status;

    /**
     * The time the Channel Points Prediction locked.
     */
    private Instant lockedAt;

    /**
     * The time the Channel Points Prediction ended.
     */
    private Instant endedAt;

}
