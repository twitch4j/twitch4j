package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.Instant;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HypeLevelUp {
    @JsonIgnore
    private Instant timeToExpire;
    private HypeTrainProgress progress;

    @JsonProperty("time_to_expire")
    private void unpackTimeToExpire(final Long timeToExpire) {
        if (timeToExpire != null)
            this.timeToExpire = Instant.ofEpochMilli(timeToExpire);
    }
}
