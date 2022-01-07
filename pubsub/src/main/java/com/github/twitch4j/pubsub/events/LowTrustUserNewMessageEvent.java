package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.LowTrustUserNewMessage;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class LowTrustUserNewMessageEvent extends TwitchEvent {
    String moderatorId;
    String channelId;
    LowTrustUserNewMessage data;
}
