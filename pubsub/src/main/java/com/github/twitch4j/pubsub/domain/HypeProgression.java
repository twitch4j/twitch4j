package com.github.twitch4j.pubsub.domain;

import lombok.Data;

@Data
public class HypeProgression {
    private String userId;
    private Integer sequenceId;
    private String action;
    private String source;
    private Integer quantity;
    private HypeTrainProgress progress;
}
