package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
public class HypeReward {
    private String type; // e.g., "EMOTE", "BADGE"
    private String id;
    private @Nullable Emote emote;
    private @Nullable Badge badge;
}
