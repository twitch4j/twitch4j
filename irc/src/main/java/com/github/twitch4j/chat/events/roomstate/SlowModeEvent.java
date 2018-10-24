package com.github.twitch4j.chat.events.roomstate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import com.github.twitch4j.chat.domain.ChatChannel;

/**
 * Slow Mode State Event
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class SlowModeEvent extends ChannelStatesEvent {
	/**
	 * time in seconds
	 */
	private final long time;

    /**
     * Constructor
     *
     * @param channel ChatChannel
     * @param time seconds
     */
	public SlowModeEvent(ChatChannel channel, long time) {
		super(channel, time > 0);
		this.time = time;
	}
}
