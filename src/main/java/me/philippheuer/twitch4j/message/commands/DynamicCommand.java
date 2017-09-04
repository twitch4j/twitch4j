package me.philippheuer.twitch4j.message.commands;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.philippheuer.twitch4j.events.event.irc.ChannelMessageEvent;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicCommand extends Command {
	/**
	 * Return value for echo-commands
	 */
	private String commandReturnText = "";

	/**
	 * Initalize Command
	 *
	 * @param commandName       Name for the new command.
	 * @param commandPermission Permissions required to use the new command.
	 * @param commandText       Text to write, if the new command is triggered.
	 * @see CommandPermission
	 */
	public DynamicCommand(String commandName, CommandPermission commandPermission, String commandText) {
		super();

		// Command Configuration
		setCommand(commandName);
		setCommandAliases(new String[]{});
		setCategory("dynamic");
		setDescription("Dynamic Command");
		getRequiredPermissions().add(commandPermission);
		setUsageExample("");
		setCommandReturnText(commandText);
	}

	/**
	 * executeCommand Logic
	 */
	@Override
	public void executeCommand(ChannelMessageEvent messageEvent) {
		super.executeCommand(messageEvent);

		// Prepare Response
		String response = commandReturnText;

		// Send Response
		getTwitchClient().getMessageInterface().sendMessage(messageEvent.getChannel().getName(), response);
	}
}
