package com.github.twitch4j.common.events.user;

import com.github.twitch4j.common.enums.CommandPermission;
import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

import java.util.Set;

/**
 * This event gets called when the bot gets a private message.
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class PrivateMessageEvent extends TwitchEvent {

	/**
	 * User
	 */
	private final EventUser user;

	/**
	 * Message
	 */
	private final String message;

	/**
	 * Permissions of the user
	 */
	private final Set<CommandPermission> permissions;

	/**
	 * Event Constructor
	 *
	 * @param user        The user who triggered the event.
	 * @param message     The plain text of the message.
	 * @param permissions The permissions of the triggering user.
	 */
	public PrivateMessageEvent(EventUser user, String message, Set<CommandPermission> permissions) {
		this.user = user;
		this.message = message;
		this.permissions = permissions;
	}

}
