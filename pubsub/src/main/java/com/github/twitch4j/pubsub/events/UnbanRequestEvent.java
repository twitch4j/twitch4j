package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.UnbanRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class UnbanRequestEvent extends TwitchEvent {
    private final String userId;
    private final String channelId;
    private final UnbanRequest unbanRequest;
}
