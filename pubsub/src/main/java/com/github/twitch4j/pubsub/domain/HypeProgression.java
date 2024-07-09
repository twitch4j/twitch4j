package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Setter(onMethod_ = { @Deprecated })
public class HypeProgression {
    private String id;
    private String userId;
    private String userLogin;
    private String userDisplayName;
    private String userProfileImageUrl;
    private Integer sequenceId;
    private String action;
    private String source;
    private Integer quantity;
    private HypeTrainProgress progress;
    @Accessors(fluent = true)
    @JsonProperty("is_boost_train")
    private Boolean isBoostTrain;
    @JsonProperty("is_large_event")
    private boolean largeEvent;
    @JsonProperty("is_fast_mode")
    private boolean fastMode;
}
