package twitch4j.chat.events.channel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import twitch4j.chat.domain.ChatChannel;
import twitch4j.chat.domain.ChatUser;
import twitch4j.chat.events.AbstractChannelEvent;

/**
 * This event gets called when a user get a timeout.
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class UserTimeoutEvent extends AbstractChannelEvent {

	/**
	 * Event Target User
	 */
	private ChatUser user;

	/**
	 * Duration in Minutes
	 */
	private Integer duration;

	/**
	 * Reason for Punishment
	 */
	private String reason;

	/**
	 * Event Constructor
	 *
	 * @param channel  The channel that this event originates from.
	 * @param user     The user who triggered the event.
	 * @param duration Timeout Duration in Minutes.
	 * @param reason   Reason for Ban.
	 */
	public UserTimeoutEvent(ChatChannel channel, ChatUser user, Integer duration, String reason) {
		super(channel);
		this.user = user;
		this.duration = duration;
		this.reason = reason;
	}
}
