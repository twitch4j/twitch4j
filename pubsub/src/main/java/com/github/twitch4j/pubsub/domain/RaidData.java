package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RaidData {
    private String id;
    private String creatorId;
    private String sourceId;
    private String targetId;
    private String targetLogin;
    private String targetDisplayName;
    private String targetProfileImage;
    private Integer transitionJitterSeconds;
    private Integer forceRaidNowSeconds;
    private Integer viewerCount;
}
