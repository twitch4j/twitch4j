package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.NONE)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Prediction {
    private String id;
    private String eventId;
    private String outcomeId;
    private String channelId;
    private Integer points;
    private Instant predictedAt;
    private Instant updatedAt;
    private String userId;
    private PredictionResult result; // can be null
    private String userDisplayName;
}
