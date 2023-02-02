package com.github.twitch4j.pubsub.domain;

import com.github.twitch4j.common.annotation.Unofficial;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@Unofficial
public class BannedTermAdded {
    private String id;
    private SimpleUser requester;
    private String term;
    private Instant activeAt;
}
