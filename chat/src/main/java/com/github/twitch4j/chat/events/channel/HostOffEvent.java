package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * This event gets called when the user stops hosting someone.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HostOffEvent extends AbstractChannelEvent {

    /**
     * Event Constructor
     *
     * @param channel The channel that this event originates from.
     */
    public HostOffEvent(EventChannel channel) {
        super(channel);
    }

}
