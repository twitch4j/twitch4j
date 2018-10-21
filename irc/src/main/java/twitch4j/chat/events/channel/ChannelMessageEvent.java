package twitch4j.chat.events.channel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import twitch4j.chat.commands.CommandPermission;
import twitch4j.chat.domain.Channel;
import twitch4j.chat.domain.User;
import twitch4j.chat.events.AbstractChannelEvent;

import java.util.Set;

/**
 * This event gets called when a message is received in a channel.
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class ChannelMessageEvent extends AbstractChannelEvent {

	/**
	 * User
	 */
	private User user;

	/**
	 * Message
	 */
	private String message;

	/**
	 * Permissions of the user
	 */
	private Set<CommandPermission> permissions;

	/**
	 * Event Constructor
	 *
	 * @param channel     The channel that this event originates from.
	 * @param user        The user who triggered the event.
	 * @param message     The plain text of the message.
	 * @param permissions The permissions of the triggering user.
	 */
	public ChannelMessageEvent(Channel channel, User user, String message, Set<CommandPermission> permissions) {
		super(channel);
		this.user = user;
		this.message = message;
		this.permissions = permissions;
	}
}
