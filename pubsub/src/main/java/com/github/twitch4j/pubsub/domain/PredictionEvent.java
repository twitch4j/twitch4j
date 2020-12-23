package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Data
@Setter(AccessLevel.NONE)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PredictionEvent {
    private String id;
    private String channelId;
    private Instant createdAt;
    private PredictionTrigger createdBy;
    private Instant endedAt;
    private PredictionTrigger endedBy;
    private Instant lockedAt;
    private PredictionTrigger lockedBy;
    private List<PredictionOutcome> outcomes;
    private Integer predictionWindowSeconds;
    /**
     * The status of the prediction (e.g., "ACTIVE", "CANCELED", "CANCEL_PENDING", "LOCKED", "RESOLVE_PENDING", "RESOLVED")
     */
    private String status;
    private String title;
    private String winningOutcomeId;
}
