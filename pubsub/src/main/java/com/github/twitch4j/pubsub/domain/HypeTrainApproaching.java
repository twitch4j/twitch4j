package com.github.twitch4j.pubsub.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class HypeTrainApproaching {
    private String channelId;
    private Integer goal;
    private Map<String, Long> eventsRemainingDurations;
    private List<HypeTrainReward> levelOneRewards;
}
