package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

@Data
@Setter(onMethod_ = { @Deprecated })
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class HypeTrainStart { // TODO(2.0.0): rename to HypeTrainExecution
    @Setter
    @Deprecated
    private String channelId;
    private String id;
    private HypeTrainConfig config;
    @Setter
    @Deprecated
    @JsonIgnore
    private HypeTrainParticipations participations;
    @JsonProperty("participations")
    private List<HypeTrainParticipation> contributions;
    private List<HypeConductor> conductors;
    private HypeTrainProgress progress;
    private Instant startedAt;
    private Instant expiresAt;
    private Instant updatedAt;
    private HypeTrainCompleted allTimeHigh;
    @Accessors(fluent = true)
    @JsonProperty("isGoldenKappaTrain")
    private Boolean isGoldenKappaTrain;
    @Accessors(fluent = true)
    @JsonProperty("isFastMode")
    private Boolean isFastMode;
    @Accessors(fluent = true)
    @JsonProperty("is_boost_train")
    @Setter
    @Deprecated
    private Boolean isBoostTrain;
}
