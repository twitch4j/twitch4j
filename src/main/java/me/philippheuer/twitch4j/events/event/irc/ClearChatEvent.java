package me.philippheuer.twitch4j.events.event.irc;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import me.philippheuer.twitch4j.events.event.AbstractChannelEvent;
import me.philippheuer.twitch4j.model.Channel;

@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class ClearChatEvent extends AbstractChannelEvent {

	/**
	 * Event Constructor
	 *
	 * @param channel     The channel that this event originates from.
	 */
	public ClearChatEvent(Channel channel) {
		super(channel);
	}
}
