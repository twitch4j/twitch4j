package me.philippheuer.twitch4j.events.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.enums.CommandPermission;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;

import java.util.Set;

@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class PrivateMessageEvent extends Event {

	/**
	 * User
	 */
	private final User user;

	/**
	 * Message Recipient
	 */
	private final User recipient;

	/**
	 * Message
	 */
	private final String message;

	/**
	 * Permissions of the user
	 */
	private final Set<CommandPermission> permissions;

	/**
	 * Constructor
	 */
	public PrivateMessageEvent(User user, User recipient, String message, Set<CommandPermission> permissions) {
		this.user = user;
		this.recipient = recipient;
		this.message = message;
		this.permissions = permissions;
	}
}
