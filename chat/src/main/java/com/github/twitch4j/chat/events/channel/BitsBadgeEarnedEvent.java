package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * Called when a user earns a new bits badge tier.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BitsBadgeEarnedEvent extends AbstractChannelEvent {
    /**
     * The user that earned the bits badge.
     */
    EventUser user;

    /**
     * The tier of the bits badge the user just earned; e.g. 100, 1000, 10000.
     */
    int bitsThreshold;

    /**
     * Event Constructor
     *
     * @param channel       The channel that this event originates from.
     * @param user          The user that earned the bits badge.
     * @param bitsThreshold The tier of the bits badge the user just earned.
     */
    public BitsBadgeEarnedEvent(EventChannel channel, EventUser user, int bitsThreshold) {
        super(channel);
        this.user = user;
        this.bitsThreshold = bitsThreshold;
    }
}
