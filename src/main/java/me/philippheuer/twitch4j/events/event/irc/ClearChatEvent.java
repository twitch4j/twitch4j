package me.philippheuer.twitch4j.events.event.irc;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import me.philippheuer.twitch4j.events.event.ChannelBaseEvent;
import me.philippheuer.twitch4j.model.Channel;

@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class ClearChatEvent extends ChannelBaseEvent {

	/**
	 * Event Constructor
	 *
	 * @param channel The channel that this event originates from.
	 */
	public ClearChatEvent(Channel channel) {
		super(channel);
	}
}
