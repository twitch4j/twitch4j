package com.github.twitch4j.pubsub.domain;

import lombok.Data;

import java.time.Instant;

@Data
public class HypeTrainReward {
    private String type; // e.g. "EMOTE"
    private String id;
    private String groupId;
    private Integer rewardLevel;
    private String setId;
    private String token;
    private Instant rewardEndDate; // 0001-01-01T00:00:00Z corresponds to forever

    public boolean isTemporary() {
        return rewardEndDate != null && rewardEndDate.isAfter(Instant.EPOCH);
    }
}
