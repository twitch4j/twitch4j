package com.github.twitch4j.chat.events.roomstate;

import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * Slow Mode State Event
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SlowModeEvent extends ChannelStatesEvent {
	/**
	 * Time in seconds
	 */
    long time;

    /**
     * Constructor
     *
     * @param channel ChatChannel
     * @param time seconds
     */
	public SlowModeEvent(EventChannel channel, long time) {
		super(channel, time > 0);
		this.time = time;
	}
}
