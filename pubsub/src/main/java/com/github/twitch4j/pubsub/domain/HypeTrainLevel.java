package com.github.twitch4j.pubsub.domain;

import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@Data
@Setter(onMethod_ = { @Deprecated })
public class HypeTrainLevel {
    private Integer value;
    private Integer goal;
    private List<HypeTrainReward> rewards;
    @Deprecated // no longer sent after switch to v2 topic
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    private Integer impressions;
}
