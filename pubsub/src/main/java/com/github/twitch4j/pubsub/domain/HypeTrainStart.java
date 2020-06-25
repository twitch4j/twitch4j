package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.Instant;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HypeTrainStart {
    private String channelId;
    private String id;
    private HypeTrainConfig config;
    private HypeTrainParticipations participations;
    private HypeTrainProgress progress;
    private Instant startedAt;
    private Instant expiresAt;
    private Instant updatedAt;
}
