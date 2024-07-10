package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
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
