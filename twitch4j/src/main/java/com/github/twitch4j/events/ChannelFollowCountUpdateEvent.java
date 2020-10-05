package com.github.twitch4j.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class ChannelFollowCountUpdateEvent extends TwitchEvent {
    EventChannel channel;
    Integer followCount;
    Integer previousFollowCount;
}
