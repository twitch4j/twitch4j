package com.github.twitch4j.eventsub.condition;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import static org.apache.commons.lang3.StringUtils.defaultString;

@Data
@Setter(AccessLevel.PRIVATE)
@SuperBuilder
@ToString(callSuper = true)
@Jacksonized
public class CustomRewardEventSubCondition extends ChannelEventSubCondition {

    /**
     * Specify a reward id to only receive notifications for a specific reward.
     */
    private String rewardId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomRewardEventSubCondition)) return false;
        if (!super.equals(o)) return false;

        CustomRewardEventSubCondition that = (CustomRewardEventSubCondition) o;
        return defaultString(rewardId).equals(defaultString(that.rewardId));
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 31 + defaultString(rewardId).hashCode();
    }
}
