package com.github.twitch4j.chat.events.roomstate;

import com.github.twitch4j.chat.domain.ChatChannel;
import com.github.twitch4j.chat.events.AbstractChannelEvent;
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

	public ChannelStatesEvent(ChatChannel channel, boolean active) {
		super(channel);
		this.active = active;
	}
}
