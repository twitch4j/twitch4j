package com.github.twitch4j.eventsub.domain.chat;

import com.github.twitch4j.common.enums.SubscriptionPlan;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class Subscription {

    /**
     * The total number of months the user has subscribed.
     */
    private Integer cumulativeMonths;

    /**
     * The number of months the subscription is for.
     */
    private Integer durationMonths;

    /**
     * The type of subscription plan being used.
     */
    private SubscriptionPlan subPlan;

}
