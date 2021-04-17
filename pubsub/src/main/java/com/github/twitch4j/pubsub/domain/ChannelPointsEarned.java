package com.github.twitch4j.pubsub.domain;

import lombok.Data;

import java.time.Instant;

@Data
public class ChannelPointsEarned {
    private Instant timestamp;
    private String channelId;
    private ChannelPointsGain pointGain;
    private ChannelPointsBalance balance;
}
