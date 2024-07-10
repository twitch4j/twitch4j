package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@Setter(onMethod_ = { @Deprecated })
public class HypeTrainApproaching {
    private String channelId;
    private Integer goal;
    private Map<String, Long> eventsRemainingDurations;
    private List<HypeTrainReward> levelOneRewards;
    private String creatorColor;
    @JsonProperty("participants")
    private List<String> participantUserIds;
    private String approachingHypeTrainId;
    private Instant expiresAt;
    @JsonProperty("is_boost_train")
    private boolean boostTrain;
    @JsonProperty("is_golden_kappa_train")
    private boolean goldenKappaTrain;
}
