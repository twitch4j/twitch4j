package com.github.twitch4j.eventsub.domain.chat;

import com.github.twitch4j.common.enums.SubscriptionPlan;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
public class SubGift {

    /**
     * The number of months the subscription is for.
     */
    private Integer durationMonths;

    /**
     * The amount of gifts the gifter has given in this channel.
     * Null if anonymous.
     */
    @Nullable
    private Integer cumulativeTotal;

    /**
     * The user ID of the subscription gift recipient.
     */
    private String recipientUserId;

    /**
     * The user name of the subscription gift recipient.
     */
    private String recipientUserName;

    /**
     * The user login of the subscription gift recipient.
     */
    private String recipientUserLogin;

    /**
     * The type of subscription plan being used.
     * <p>
     * Cannot be {@link SubscriptionPlan#TWITCH_PRIME}.
     */
    private SubscriptionPlan subTier;

    /**
     * The ID of the associated community gift.
     * Null if not associated with a community gift.
     *
     * @see CommunitySubGift#getId()
     */
    @Nullable
    private String communityGiftId;

}
