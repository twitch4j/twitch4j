package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class PredictionTrigger {
    private String type;
    private String userId;
    private String userDisplayName;
    private String extensionClientId;
}
