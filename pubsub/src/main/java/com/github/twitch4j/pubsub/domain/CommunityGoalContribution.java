package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class CommunityGoalContribution {
    private String channelId;
    private CommunityPointsGoal goal;
    private ChannelPointsUser user;
    private Integer amount;
    private Integer streamContribution;
    private Integer totalContribution;
}
