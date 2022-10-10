package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.UpdatedPinnedChatTiming;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class PinnedChatTimingUpdatedEvent extends TwitchEvent {
    String channelId;
    UpdatedPinnedChatTiming data;
}
