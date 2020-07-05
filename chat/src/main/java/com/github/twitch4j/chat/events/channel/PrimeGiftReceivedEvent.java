package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * Called when a user receives a gift from a Prime member.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Unofficial
public class PrimeGiftReceivedEvent extends AbstractChannelEvent {
    /**
     * The user giving the gift.
     */
    EventUser gifter;

    /**
     * The name of the gift package.
     */
    String giftName;

    /**
     * The display name of the user receiving the gift.
     */
    String recipientName;

    /**
     * Event Constructor
     *
     * @param channel       The channel that this event originates from.
     * @param gifter        The user giving the gift.
     * @param giftName      The name of the gift package.
     * @param recipientName The user receiving the gift.
     */
    public PrimeGiftReceivedEvent(EventChannel channel, EventUser gifter, String giftName, String recipientName) {
        super(channel);
        this.gifter = gifter;
        this.giftName = giftName;
        this.recipientName = recipientName;
    }
}
