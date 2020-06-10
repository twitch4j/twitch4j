package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.pubsub.domain.ChannelPointsReward;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CustomRewardCreatedEvent extends CustomRewardEvent {
    public CustomRewardCreatedEvent(Instant timestamp, ChannelPointsReward reward) {
        super(timestamp, reward);
    }
}
