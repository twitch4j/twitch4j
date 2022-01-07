package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.LowTrustUserTreatmentUpdate;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class LowTrustUserTreatmentUpdateEvent extends TwitchEvent {
    String moderatorId;
    String channelId;
    LowTrustUserTreatmentUpdate data;
}
