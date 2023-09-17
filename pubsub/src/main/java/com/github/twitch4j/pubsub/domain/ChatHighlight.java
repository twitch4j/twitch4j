package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class ChatHighlight {
    private String type;
    private String sourceChannelId;
    private Long secondsSinceEvent;

    public boolean isRaider() {
        return "raider".equalsIgnoreCase(type);
    }
}
