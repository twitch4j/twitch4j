package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.twitch4j.common.util.MilliInstantDeserializer;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

@Data
@Setter(onMethod_ = { @Deprecated })
public class HypeTrainEnd {
    private String id;
    @JsonDeserialize(using = MilliInstantDeserializer.class)
    private Instant endedAt;
    private String endingReason;
    @Accessors(fluent = true)
    @JsonProperty("is_boost_train")
    private Boolean isBoostTrain;
    private List<HypeTrainParticipation> participationTotals;
    private List<HypeReward> rewards;
}
