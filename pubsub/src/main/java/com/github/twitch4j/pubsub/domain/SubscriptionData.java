package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionData {
    private String userName;
    private String displayName;
    private String channelName;
    private String userId;
    private String channelId;
    private String time;
    private String subPlan; // "Prime"/"1000"/"2000"/"3000"
    private String subPlanName;
    @Deprecated
    private Integer months;
    private Integer cumulativeMonths;
    private Integer streakMonths;
    private String context; // "sub"/"resub"/"subgift"/"anonsubgift"
    private CommerceMessage subMessage;
    private String recipientId;
    private String recipientUserName;
    private String recipientDisplayName;
}
