package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * Called when a monetary event (e.g. sub gift or cheer) results in rewards (e.g. emotes) being shared with others.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RewardGiftEvent extends AbstractChannelEvent {
    /**
     * The user that triggered this event.
     */
    EventUser user;

    /**
     * The domain of the rewards being gifted (e.g. "pride_megacommerce_2020").
     */
    String domain;

    /**
     * The type of monetary event that triggered the reward gift (e.g., "SUBGIFT", "CHEER").
     */
    String triggerType;

    /**
     * The number of gifted rewards as part of the primary selection.
     */
    Integer selectedCount;

    /**
     * The total number of rewards being gifted (e.g. 5 emotes).
     */
    Integer totalRewardCount;

    /**
     * The number of instances of the trigger (e.g. 1 sub gift or 300 bits).
     */
    Integer triggerAmount;

    /**
     * Event Constructor
     *
     * @param channel          The channel that this event originates from.
     * @param user             The user that triggered this event.
     * @param domain           The domain of the rewards being gifted.
     * @param triggerType      The type of monetary event that triggered the reward gift.
     * @param selectedCount    The number of gifted rewards as part of the primary selection.
     * @param totalRewardCount The total number of rewards being gifted.
     * @param triggerAmount    The number of instances of the trigger.
     */
    public RewardGiftEvent(EventChannel channel, EventUser user, String domain, String triggerType, Integer selectedCount, Integer totalRewardCount, Integer triggerAmount) {
        super(channel);
        this.user = user;
        this.domain = domain;
        this.triggerType = triggerType;
        this.selectedCount = selectedCount;
        this.totalRewardCount = totalRewardCount;
        this.triggerAmount = triggerAmount;
    }
}
