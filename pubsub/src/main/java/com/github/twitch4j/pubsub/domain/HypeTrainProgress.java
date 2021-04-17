package com.github.twitch4j.pubsub.domain;

import lombok.Data;

@Data
public class HypeTrainProgress {
    private HypeTrainLevel level;
    private Integer value;
    private Integer goal;
    private Integer total;
    private Integer remainingSeconds;
}
