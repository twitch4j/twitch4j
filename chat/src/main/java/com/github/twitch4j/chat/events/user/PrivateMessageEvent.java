package com.github.twitch4j.chat.events.user;

import com.github.twitch4j.chat.commands.CommandPermission;
import com.github.twitch4j.chat.events.TwitchEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import com.github.twitch4j.chat.domain.ChatUser;

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
	private final ChatUser user;

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
	public PrivateMessageEvent(ChatUser user, String message, Set<CommandPermission> permissions) {
		this.user = user;
		this.message = message;
		this.permissions = permissions;
	}

	public void sendResponse(String message) {
		// getClient().getMessageInterface().sendPrivateMessage(user.getName(), message);
	}
}
