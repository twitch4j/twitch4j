package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class CommunityBoostProgression {
    private String channelId;
    private List<CommunityBoostOrder> boostOrders;
    private Integer totalGoalProgress;
    private Integer totalGoalTarget;
}
