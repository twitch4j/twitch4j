package twitch4j.chat.events.channel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import twitch4j.chat.domain.Channel;
import twitch4j.chat.events.AbstractChannelEvent;

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
