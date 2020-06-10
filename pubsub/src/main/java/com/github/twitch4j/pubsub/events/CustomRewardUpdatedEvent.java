package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.pubsub.domain.ChannelPointsReward;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CustomRewardUpdatedEvent extends CustomRewardEvent {
    public CustomRewardUpdatedEvent(Instant timestamp, ChannelPointsReward reward) {
        super(timestamp, reward);
    }
}
