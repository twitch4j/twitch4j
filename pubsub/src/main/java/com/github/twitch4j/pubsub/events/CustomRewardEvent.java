package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.ChannelPointsReward;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class CustomRewardEvent extends TwitchEvent {
    private final Instant timestamp;
    private final ChannelPointsReward reward;
}
