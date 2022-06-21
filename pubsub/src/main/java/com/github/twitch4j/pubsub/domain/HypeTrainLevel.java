package com.github.twitch4j.pubsub.domain;

import lombok.Data;

import java.util.List;

@Data
public class HypeTrainLevel {
    private Integer value;
    private Integer goal;
    private List<HypeTrainReward> rewards;
    private Integer impressions;
}
