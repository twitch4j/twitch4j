package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.SharedChatSession;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class SharedChatPubSubEvent extends TwitchEvent {
    private final String channelId;
    private final Instant timestamp;
    private final SharedChatSession session;
}
