package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.OutboundUnfollow;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class OutboundUnfollowPubSubEvent extends TwitchEvent {
    String sourceUserId;
    OutboundUnfollow data;
}
