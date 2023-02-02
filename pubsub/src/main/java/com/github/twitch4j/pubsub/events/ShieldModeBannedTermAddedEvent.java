package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.BannedTermAdded;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class ShieldModeBannedTermAddedEvent extends TwitchEvent {
    String userId;
    String channelId;
    BannedTermAdded data;
}
