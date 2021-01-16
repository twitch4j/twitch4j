package com.github.twitch4j.pubsub.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.Prediction;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.time.Instant;

/**
 * Sent on prediction-result for predictions-user-v1
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Setter(AccessLevel.NONE)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPredictionResultEvent extends TwitchEvent {
    private Instant timestamp;
    private Prediction prediction;
}
