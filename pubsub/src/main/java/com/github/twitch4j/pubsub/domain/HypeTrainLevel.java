package com.github.twitch4j.pubsub.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Setter(onMethod_ = { @Deprecated })
public class HypeTrainLevel {
    private Integer value;
    private Integer goal;
    private List<HypeTrainReward> rewards;
    @Getter(onMethod_ = { @Deprecated }) // no longer sent after switch to v2 topic
    private Integer impressions;
}
