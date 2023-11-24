package com.github.twitch4j.eventsub.domain.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.common.enums.SubscriptionPlan;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Setter(AccessLevel.PRIVATE)
public class Subscription {

    /**
     * The number of months the subscription is for.
     */
    private Integer durationMonths;

    /**
     * The type of subscription plan being used.
     * <p>
     * Does not contain {@link SubscriptionPlan#TWITCH_PRIME}; use {@link #isPrime()} instead.
     */
    private SubscriptionPlan subTier;

    /**
     * Indicates if the subscription was obtained through Amazon Prime.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_prime")
    private Boolean isPrime;

}
