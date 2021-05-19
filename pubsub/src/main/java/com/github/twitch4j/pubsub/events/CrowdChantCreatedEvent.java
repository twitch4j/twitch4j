package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.CrowdChant;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
public class CrowdChantCreatedEvent extends TwitchEvent {
    private Instant timestamp;
    private CrowdChant crowdChant;
}
