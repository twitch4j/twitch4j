package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelBitsData {
    private String userId;
    private String userName;
    private String channelId;
    private String channelName;
    private String time;
    private String chatMessage;
    private Integer bitsUsed;
    private Integer totalBitsUsed;
    private String context;
    private BadgeEntitlement badgeEntitlement;

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BadgeEntitlement {
        private int previousVersion;
        private int newVersion;
    }
}
