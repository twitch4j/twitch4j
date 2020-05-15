package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommerceData {
    private String userName;
    private String displayName;
    private String channelName;
    private String userId;
    private String channelId;
    private String time;
    private String itemImageUrl;
    private String itemDescription;
    private Boolean supportsChannel;
    private PurchaseMessage purchaseMessage;

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PurchaseMessage {
        private String message;
        private List<CommerceEmote> emotes;
    }

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CommerceEmote {
        private Integer start;
        private Integer end;
        private Integer id;
    }
}
