package me.philippheuer.twitch4j.events.event.irc.roomstates;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.philippheuer.twitch4j.events.event.ChannelBaseEvent;
import me.philippheuer.twitch4j.model.Channel;

@Data
@Getter
@EqualsAndHashCode(callSuper = false)
public abstract class ChannelStatesBaseEvent extends ChannelBaseEvent {

	private final boolean active;

	public ChannelStatesBaseEvent(Channel channel, boolean active) {
		super(channel);
		this.active = active;
	}
}
