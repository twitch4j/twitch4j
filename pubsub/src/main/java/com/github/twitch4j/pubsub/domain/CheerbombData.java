package com.github.twitch4j.pubsub.domain;

import lombok.Data;
import lombok.Setter;

@Data
@Setter(onMethod_ = { @Deprecated })
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
