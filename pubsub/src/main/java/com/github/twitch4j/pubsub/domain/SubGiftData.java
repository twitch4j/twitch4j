package com.github.twitch4j.pubsub.domain;

import com.github.twitch4j.common.enums.SubscriptionPlan;
import lombok.Data;

@Data
public class SubGiftData {
    private Integer count;
    private SubscriptionPlan tier;
    private String userId;
    private String userName;
    private String displayName;
    private String channelId;
    private String uuid;
    private String type; // e.g., "mystery-gift-purchase"
}
