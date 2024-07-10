package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class HypeTrainConductorReward {
    private String source; // e.g., "EXPLICIT_PURCHASE", "BITS", "SUBS"
    private String type; // e.g., "CURRENT", "FORMER"
    private List<HypeReward> rewards;
}
