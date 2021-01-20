package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.PredictionEvent;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = false)
@Setter(AccessLevel.NONE)
public class PredictionCreatedEvent extends TwitchEvent {
    private Instant timestamp;
    private PredictionEvent event;
}
