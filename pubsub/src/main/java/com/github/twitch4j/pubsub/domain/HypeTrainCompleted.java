package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class HypeTrainCompleted {
    private int goal;
    private int progression;
    private int total;
    private HypeTrainLevel level;
}
