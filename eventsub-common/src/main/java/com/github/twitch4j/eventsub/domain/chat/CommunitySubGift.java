package com.github.twitch4j.eventsub.domain.chat;

import com.github.twitch4j.common.enums.SubscriptionPlan;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
public class CommunitySubGift {

    /**
     * The ID of the associated community gift.
     *
     * @see SubGift#getCommunityGiftId()
     */
    private String id;

    /**
     * Number of subscriptions being gifted.
     */
    private Integer total;

    /**
     * The type of subscription plan being used.
     * <p>
     * Cannot be {@link SubscriptionPlan#TWITCH_PRIME}.
     */
    private SubscriptionPlan subTier;

    /**
     * The amount of gifts the gifter has given in this channel.
     * Null if anonymous.
     */
    @Nullable
    private Integer cumulativeTotal;

}
