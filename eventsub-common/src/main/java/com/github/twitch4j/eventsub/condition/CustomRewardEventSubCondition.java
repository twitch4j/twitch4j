package com.github.twitch4j.eventsub.condition;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Setter(AccessLevel.PRIVATE)
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Jacksonized
public class CustomRewardEventSubCondition extends ChannelEventSubCondition {

    /**
     * Specify a reward id to only receive notifications for a specific reward.
     */
    private String rewardId;

}
