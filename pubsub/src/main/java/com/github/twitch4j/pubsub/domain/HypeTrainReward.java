package com.github.twitch4j.pubsub.domain;

import lombok.Data;

@Data
public class HypeTrainReward {
    private String type;
    private String id;
    private String groupId;
    private Integer rewardLevel;
    private String setId;
    private String token;
}
