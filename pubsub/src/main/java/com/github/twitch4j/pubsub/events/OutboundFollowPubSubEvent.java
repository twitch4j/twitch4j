package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.OutboundFollow;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class OutboundFollowPubSubEvent extends TwitchEvent {
    String sourceUserId;
    OutboundFollow data;
}
