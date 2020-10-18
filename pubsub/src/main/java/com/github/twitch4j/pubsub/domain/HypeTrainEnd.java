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
public class HypeTrainEnd {
    @JsonDeserialize(using = MilliInstantDeserializer.class)
    private Instant endedAt;
    private String endingReason;
}
