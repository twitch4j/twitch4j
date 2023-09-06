package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class ScheduledAd {
    private Instant runAtTime;
    private Long durationSeconds;
}
