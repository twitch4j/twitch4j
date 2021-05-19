package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class CrowdChant {
    private String id;
    private String channelId;
    private ChannelPointsUser user;
    private String chatMessageId;
    private String text;
    private Instant createdAt;
    private Instant endsAt;
}
