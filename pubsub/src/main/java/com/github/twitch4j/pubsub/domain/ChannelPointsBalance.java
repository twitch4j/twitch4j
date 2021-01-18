package com.github.twitch4j.pubsub.domain;

import lombok.Data;

@Data
public class ChannelPointsBalance {
    private String userId;
    private String channelId;
    private Long balance;
}
