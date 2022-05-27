package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.twitch4j.common.util.MilliInstantDeserializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
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
    @Accessors(fluent = true)
    @JsonProperty("is_boost_train")
    private Boolean isBoostTrain;
}
