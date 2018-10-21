package twitch4j.chat.events.channel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import twitch4j.chat.domain.Channel;
import twitch4j.chat.domain.User;
import twitch4j.chat.events.AbstractChannelEvent;

/**
 * This event gets called when a user gets a new followers
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class FollowEvent extends AbstractChannelEvent {

	/**
	 * User
	 */
	private User user;

	/**
	 * Event Constructor
	 *
	 * @param channel The channel that this event originates from.
	 * @param user    The user who triggered the event.
	 */
	public FollowEvent(Channel channel, User user) {
		super(channel);
		this.user = user;
	}
}
