package com.github.twitch4j.eventsub.domain.chat;

import com.github.twitch4j.common.enums.SubscriptionPlan;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class PrimePaidUpgrade {

    /**
     * The type of subscription plan being used.
     * <p>
     * Cannot be {@link SubscriptionPlan#TWITCH_PRIME}.
     */
    private SubscriptionPlan subTier;

}
