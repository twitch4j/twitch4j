package twitch4j.chat.events.channel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import twitch4j.chat.commands.CommandPermission;
import twitch4j.chat.domain.ChatChannel;
import twitch4j.chat.domain.ChatUser;
import twitch4j.chat.events.AbstractChannelEvent;

import java.util.Set;

/**
 * This event gets called when a action message (/me text) is received in a channel.
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class ChannelMessageActionEvent extends AbstractChannelEvent {

	/**
	 * User
	 */
	private ChatUser user;

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
	public ChannelMessageActionEvent(ChatChannel channel, ChatUser user, String message, Set<CommandPermission> permissions) {
		super(channel);
		this.user = user;
		this.message = message;
		this.permissions = permissions;
	}
}
