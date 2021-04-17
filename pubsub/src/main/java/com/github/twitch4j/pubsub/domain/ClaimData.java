package com.github.twitch4j.pubsub.domain;

import lombok.Data;

import java.time.Instant;

@Data
public class ClaimData {
    private Instant timestamp;
    private Claim claim;

    @Data
    public static class Claim {
        private String id;
        private String userId;
        private String channelId;
        private ChannelPointsGain pointGain;
        private Instant createdAt;
    }
}
