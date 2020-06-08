package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.HypeTrainEnd;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HypeTrainEndEvent extends TwitchEvent {
    private final String channelId;
    private final HypeTrainEnd data;
}
