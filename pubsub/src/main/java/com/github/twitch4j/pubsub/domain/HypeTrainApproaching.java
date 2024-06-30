package com.github.twitch4j.pubsub.domain;

import lombok.Data;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Data
@Setter(onMethod_ = { @Deprecated })
public class HypeTrainApproaching {
    private String channelId;
    private Integer goal;
    private Map<String, Long> eventsRemainingDurations;
    private List<HypeTrainReward> levelOneRewards;
}
