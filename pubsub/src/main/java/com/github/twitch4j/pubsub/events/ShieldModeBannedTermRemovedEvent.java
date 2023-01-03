package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.BannedTermRemoved;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class ShieldModeBannedTermRemovedEvent extends TwitchEvent {
    String userId;
    String channelId;
    BannedTermRemoved data;
}
