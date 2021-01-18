package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.Prediction;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.time.Instant;

/**
 * Sent on prediction-made for predictions-user-v1
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Setter(AccessLevel.NONE)
public class UserPredictionMadeEvent extends TwitchEvent {
    private Instant timestamp;
    private Prediction prediction;
}
