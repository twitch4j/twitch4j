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
public class MessageEvent extends Event {

	/**
	 * Event Channel
	 */
	private final Channel channel;

	/**
	 * User
	 */
	private final User user;

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
	public MessageEvent(Channel channel, User user, String message, Set<CommandPermission> permissions) {
		this.channel = channel;
		this.user = user;
		this.message = message;
		this.permissions = permissions;
	}
}
