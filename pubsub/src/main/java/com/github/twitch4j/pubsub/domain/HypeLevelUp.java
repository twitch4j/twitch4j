package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.twitch4j.common.util.MilliInstantDeserializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
public class HypeLevelUp {
    @JsonDeserialize(using = MilliInstantDeserializer.class)
    private Instant timeToExpire;
    private HypeTrainProgress progress;
    @Accessors(fluent = true)
    @JsonProperty("is_boost_train")
    private Boolean isBoostTrain;
    private HypeTrainStart hypeTrain;
}
