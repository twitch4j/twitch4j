package com.github.twitch4j.chat.events.roomstate;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Abstract Channel State Event
 */
@Data
@Getter
@EqualsAndHashCode(callSuper = false)
public abstract class ChannelStatesEvent extends AbstractChannelEvent {

	private final boolean active;

	public ChannelStatesEvent(EventChannel channel, boolean active) {
		super(channel);
		this.active = active;
	}
}
