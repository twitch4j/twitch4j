package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.AutomodLevelsModified;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class AutomodLevelsModifiedEvent extends TwitchEvent {
    String channelId;
    AutomodLevelsModified data;
}
