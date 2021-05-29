package com.github.twitch4j.eventsub.events;

import com.github.twitch4j.common.enums.SubscriptionPlan;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelSubscriptionGiftEvent extends EventSubUserChannelEvent {

    /**
     * The tier of subscriptions in the subscription gift.
     */
    private SubscriptionPlan tier;

    /**
     * The number of subscriptions in the subscription gift.
     */
    private Integer total;

    /**
     * The number of subscriptions gifted by this user in the channel.
     * This value is null for anonymous gifts or if the gifter has opted out of sharing this information.
     */
    @Nullable
    private Integer cumulativeTotal;

}
