package com.github.twitch4j.pubsub.domain;

import lombok.Data;

@Data
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
