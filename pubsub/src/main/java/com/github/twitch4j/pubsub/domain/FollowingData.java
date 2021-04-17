package com.github.twitch4j.pubsub.domain;

import lombok.Data;

@Data
public class FollowingData {
    private String displayName;
    private String username;
    private String userId;
}
