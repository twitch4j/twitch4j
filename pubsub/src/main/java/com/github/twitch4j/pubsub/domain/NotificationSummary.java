package com.github.twitch4j.pubsub.domain;

import lombok.Data;

import java.time.Instant;

@Data
public class NotificationSummary {
    private Integer unseenViewCount;
    private Instant lastSeenAt;
}
