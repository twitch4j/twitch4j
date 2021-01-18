package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.common.enums.SubscriptionPlan;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelSubscribeEvent extends EventSubUserChannelEvent {

    /**
     * The tier of the subscription.
     * <p>
     * Prime is treated as 1000, at the time of writing.
     */
    private SubscriptionPlan tier;

    /**
     * Whether the subscription is a gift.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_gift")
    private Boolean isGift;

}
