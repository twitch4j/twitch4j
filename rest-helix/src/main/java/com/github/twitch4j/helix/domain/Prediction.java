package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.eventsub.domain.PredictionOutcome;
import com.github.twitch4j.eventsub.domain.PredictionStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.With;
import lombok.extern.jackson.Jacksonized;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@With
@Data
@Setter(AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Prediction {

    /**
     * ID of the Prediction.
     */
    private String id;

    /**
     * ID of the broadcaster.
     */
    private String broadcasterId;

    /**
     * Display name of the broadcaster.
     */
    private String broadcasterName;

    /**
     * Login name of the broadcaster.
     */
    private String broadcasterLogin;

    /**
     * Title for the Prediction. Maximum: 45 characters.
     */
    private String title;

    /**
     * ID of the winning outcome. If the status is ACTIVE, this is set to null.
     */
    @Nullable
    private String winningOutcomeId;

    /**
     * The possible outcomes for the Prediction. Size must be 2.
     */
    @Singular
    private List<PredictionOutcome> outcomes;

    /**
     * Total duration for the Prediction (in seconds). Minimum: 1. Maximum: 1800.
     */
    @JsonProperty("prediction_window")
    private Integer predictionWindowSeconds;

    /**
     * Status of the Prediction.
     */
    private PredictionStatus status;

    /**
     * UTC timestamp for the Prediction’s start time.
     */
    private Instant createdAt;

    /**
     * UTC timestamp for when the Prediction ended. If the status is ACTIVE, this is set to null.
     */
    @Nullable
    private Instant endedAt;

    /**
     * UTC timestamp for when the Prediction was locked. If the status is not LOCKED, this is set to null.
     */
    @Nullable
    private Instant lockedAt;

    /**
     * @return the total duration for the Prediction.
     */
    @Nullable
    @JsonIgnore
    public Duration getPredictionWindow() {
        return predictionWindowSeconds != null ? Duration.ofSeconds(predictionWindowSeconds.longValue()) : null;
    }

    /**
     * @return the winning PredictionOutcome, in an optional wrapper
     */
    @JsonIgnore
    public Optional<PredictionOutcome> getWinningOutcome() {
        if (winningOutcomeId != null) {
            for (PredictionOutcome outcome : getOutcomes()) {
                if (outcome != null && winningOutcomeId.equals(outcome.getId()))
                    return Optional.of(outcome);
            }
        }
        return Optional.empty();
    }

}
