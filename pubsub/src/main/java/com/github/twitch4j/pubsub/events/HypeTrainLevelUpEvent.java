package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.HypeLevelUp;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HypeTrainLevelUpEvent extends TwitchEvent {
    private final String channelId;
    private final HypeLevelUp data;
}
