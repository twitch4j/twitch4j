package com.github.twitch4j.pubsub.domain;

import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(onMethod_ = { @Deprecated })
public class PointsSpent {
    private Instant timestamp;
    private Balance balance;

    @Data
    @Setter(onMethod_ = { @Deprecated })
    public static class Balance {
        private String userId;
        private String channelId;
        private Long balance;
    }
}
