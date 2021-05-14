package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.eventsub.domain.PredictionOutcome;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class ChannelPredictionEvent extends EventSubChannelEvent {

    /**
     * Channel Points Prediction ID.
     */
    @JsonProperty("id")
    private String predictionId;

    /**
     * Title for the Channel Points Prediction.
     */
    private String title;

    /**
     * The outcomes for the Channel Points Prediction. May or may not include top_predictors.
     */
    private List<PredictionOutcome> outcomes;

    /**
     * The time the Channel Points Prediction started.
     */
    private Instant startedAt;

}
