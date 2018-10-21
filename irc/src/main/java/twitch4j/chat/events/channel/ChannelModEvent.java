package twitch4j.chat.events.channel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import twitch4j.chat.domain.Channel;
import twitch4j.chat.domain.User;
import twitch4j.chat.events.AbstractChannelEvent;

/**
 * This event gets called when a client gains/loses mod status.
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class ChannelModEvent extends AbstractChannelEvent {

	/**
	 * User
	 */
	private User user;

	/**
	 * Is Moderator?
	 */
	private boolean isMod;

	/**
	 * Event Constructor
	 *
	 * @param channel     The channel that this event originates from.
	 * @param user 		  The user that gained/lost mod status.
	 * @param isMod		  Did the use gain or lose mod status?
	 */
	public ChannelModEvent(Channel channel, User user, boolean isMod) {
		super(channel);
		this.user = user;
		this.isMod = isMod;
	}
}
