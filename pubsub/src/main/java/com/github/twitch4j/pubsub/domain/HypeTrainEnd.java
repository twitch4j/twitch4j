package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.twitch4j.common.util.MilliInstantDeserializer;
import lombok.Data;

import java.time.Instant;

@Data
public class HypeTrainEnd {
    @JsonDeserialize(using = MilliInstantDeserializer.class)
    private Instant endedAt;
    private String endingReason;
}
