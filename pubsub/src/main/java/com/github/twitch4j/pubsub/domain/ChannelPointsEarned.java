package com.github.twitch4j.pubsub.domain;

import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(onMethod_ = { @Deprecated })
public class ChannelPointsEarned {
    private Instant timestamp;
    private String channelId;
    private ChannelPointsGain pointGain;
    private ChannelPointsBalance balance;
}
