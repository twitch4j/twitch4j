package twitch4j.chat.events.channel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import twitch4j.chat.domain.Channel;
import twitch4j.chat.events.TwitchEvent;

/**
 * This event gets called when the user stops hosting someone.
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class HostOffEvent extends TwitchEvent {

	/**
	 * Event Channel
	 */
	private Channel channel;

	/**
	 * Event Constructor
	 *
	 * @param channel     The channel that this event originates from.
	 */
	public HostOffEvent(Channel channel) {
		this.channel = channel;
	}

}
