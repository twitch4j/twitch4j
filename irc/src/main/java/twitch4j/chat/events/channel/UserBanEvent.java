package twitch4j.chat.events.channel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import twitch4j.chat.domain.ChatChannel;
import twitch4j.chat.domain.ChatUser;
import twitch4j.chat.events.AbstractChannelEvent;

/**
 * This event gets called when a user gets banned.
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class UserBanEvent extends AbstractChannelEvent {

	/**
	 * Event Target User
	 */
	private ChatUser user;

	/**
	 * Reason for Punishment
	 */
	private String reason;

	/**
	 * Event Constructor
	 *
	 * @param channel The channel that this event originates from.
	 * @param user    The user who triggered the event.
	 * @param reason  Reason for Ban.
	 */
	public UserBanEvent(ChatChannel channel, ChatUser user, String reason) {
		super(channel);
		this.user = user;
		this.reason = reason;
	}
}
