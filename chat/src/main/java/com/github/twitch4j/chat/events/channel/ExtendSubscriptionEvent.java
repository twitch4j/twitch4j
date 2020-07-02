package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.enums.SubscriptionPlan;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.Month;

/**
 * Called when a user extends their existing subscription into a future month.
 *
 * @see <a href="https://discuss.dev.twitch.tv/t/ios-sub-tokens-launch/22794">Official Announcement</a>
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExtendSubscriptionEvent extends AbstractChannelEvent {
    /**
     * The user extending their subscription.
     */
    EventUser user;

    /**
     * The type of subscription plan being used.
     */
    SubscriptionPlan subPlan;

    /**
     * The total number of months the user has subscribed.
     */
    int cumulativeMonths;

    /**
     * The new month that the subscription will end on.
     */
    Month benefitEndMonth;

    /**
     * Event Constructor
     *
     * @param channel          The channel that this event originates from.
     * @param user             The user extending their subscription.
     * @param subPlan          The type of subscription plan being used.
     * @param cumulativeMonths The total number of months the user has subscribed.
     * @param benefitEndMonth  The new month that the subscription will end on.
     */
    public ExtendSubscriptionEvent(EventChannel channel, EventUser user, SubscriptionPlan subPlan, int cumulativeMonths, Month benefitEndMonth) {
        super(channel);
        this.user = user;
        this.subPlan = subPlan;
        this.cumulativeMonths = cumulativeMonths;
        this.benefitEndMonth = benefitEndMonth;
    }
}
