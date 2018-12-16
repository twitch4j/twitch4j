package com.github.twitch4j.chat.events.roomstate;

import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

/**
 * R9K State Event
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class Robot9000Event extends ChannelStatesEvent{

    /**
     * Constructor
     *
     * @param channel ChatChannel
     * @param active State active?
     */
	public Robot9000Event(EventChannel channel, boolean active) {
		super(channel, active);
	}
}
