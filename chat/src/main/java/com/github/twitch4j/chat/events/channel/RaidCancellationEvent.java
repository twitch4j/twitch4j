package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * Called when a channel cancels an outstanding raid.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RaidCancellationEvent extends AbstractChannelEvent {
    /**
     * Event Constructor
     *
     * @param channel The channel that this event originates from.
     */
    public RaidCancellationEvent(EventChannel channel) {
        super(channel);
    }
}
