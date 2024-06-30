package com.github.twitch4j.pubsub.domain;

import lombok.Data;
import lombok.Setter;

@Data
@Setter(onMethod_ = { @Deprecated })
public class ChannelPointsBalance {
    private String userId;
    private String channelId;
    private Long balance;
}
