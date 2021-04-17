package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.twitch4j.common.util.MilliInstantDeserializer;
import lombok.Data;

import java.time.Instant;

@Data
public class HypeLevelUp {
    @JsonDeserialize(using = MilliInstantDeserializer.class)
    private Instant timeToExpire;
    private HypeTrainProgress progress;
}
