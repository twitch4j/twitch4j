package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class Prediction {
    private String id;
    private String eventId;
    private String outcomeId;
    private String channelId;
    private Integer points;
    private Instant predictedAt;
    private Instant updatedAt;
    private String userId;
    @Nullable
    private PredictionResult result;
    private String userDisplayName;
}
