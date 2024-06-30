package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class OutboundUnfollow {

    private String targetUserId;

    private Instant timestamp;

}
