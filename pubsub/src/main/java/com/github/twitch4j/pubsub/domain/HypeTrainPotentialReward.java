package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class HypeTrainPotentialReward {
    private String id;
    private int level;
    private HypeReward value;
}
