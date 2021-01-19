package com.github.twitch4j.eventsub.condition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomRewardEventSubCondition extends ChannelEventSubCondition {

    /**
     * Specify a reward id to only receive notifications for a specific reward.
     */
    private String rewardId;

}
