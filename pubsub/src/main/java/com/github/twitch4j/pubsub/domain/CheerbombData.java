package com.github.twitch4j.pubsub.domain;

import lombok.Data;

@Data
public class CheerbombData {
    private String userID;
    private String displayName;
    private String userLogin;
    private Integer selectedCount;
    private String triggerType;
    private Integer triggerAmount;
    private Integer totalRewardCount;
    private String domain;
}
