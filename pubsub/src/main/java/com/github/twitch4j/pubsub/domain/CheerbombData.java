package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
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
