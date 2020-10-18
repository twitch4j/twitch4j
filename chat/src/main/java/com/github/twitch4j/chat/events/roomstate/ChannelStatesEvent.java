package com.github.twitch4j.chat.events.roomstate;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Abstract Channel State Event
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class ChannelStatesEvent extends AbstractChannelEvent {

	private final boolean active;

	public ChannelStatesEvent(EventChannel channel, boolean active) {
		super(channel);
		this.active = active;
	}
}
