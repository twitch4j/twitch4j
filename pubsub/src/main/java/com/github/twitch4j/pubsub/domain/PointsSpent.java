package com.github.twitch4j.pubsub.domain;

import lombok.Data;

import java.time.Instant;

@Data
public class PointsSpent {
    private Instant timestamp;
    private Balance balance;

    @Data
    public static class Balance {
        private String userId;
        private String channelId;
        private Long balance;
    }
}
