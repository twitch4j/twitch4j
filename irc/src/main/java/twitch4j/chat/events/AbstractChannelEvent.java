package twitch4j.chat.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import twitch4j.chat.domain.ChatChannel;

/**
 * This event is a base for events that originate from a channel
 */
@Data
@Getter
@EqualsAndHashCode(callSuper = false)
public class AbstractChannelEvent extends TwitchEvent {

	/**
	 * Event Channel
	 */
	private final ChatChannel channel;

	/**
	 * Event Constructor
	 *
	 * @param channel The channel that this event originates from.
	 */
	public AbstractChannelEvent(ChatChannel channel) {
		super();
		this.channel = channel;
	}
}
