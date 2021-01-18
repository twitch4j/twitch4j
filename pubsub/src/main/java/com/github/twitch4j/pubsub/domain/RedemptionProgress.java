package com.github.twitch4j.pubsub.domain;

import lombok.Data;

@Data
public class RedemptionProgress {
    private String id;
    private String channelId;
    private String rewardId;
    private String method;
    private String newStatus;
    private Integer processed;
    private Integer total;
}
