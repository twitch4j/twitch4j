package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
public class HypeProgression {
    private String userId;
    private Integer sequenceId;
    private String action;
    private String source;
    private Integer quantity;
    private HypeTrainProgress progress;
    @Accessors(fluent = true)
    @JsonProperty("is_boost_train")
    private Boolean isBoostTrain;
}
