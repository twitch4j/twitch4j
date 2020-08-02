package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.twitch4j.common.util.MilliInstantDeserializer;
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
    @JsonDeserialize(using = MilliInstantDeserializer.class)
    private Instant startedAt;
    @JsonDeserialize(using = MilliInstantDeserializer.class)
    private Instant expiresAt;
    @JsonDeserialize(using = MilliInstantDeserializer.class)
    private Instant updatedAt;
}
