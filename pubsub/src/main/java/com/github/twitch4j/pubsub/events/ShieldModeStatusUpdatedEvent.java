package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.ShieldModeStatus;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class ShieldModeStatusUpdatedEvent extends TwitchEvent {
    String userId;
    String channelId;
    ShieldModeStatus data;
}
