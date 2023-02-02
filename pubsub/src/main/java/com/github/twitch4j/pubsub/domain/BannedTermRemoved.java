package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class BannedTermRemoved {
    private String id;
    private String requesterId;
    private String term;
}
