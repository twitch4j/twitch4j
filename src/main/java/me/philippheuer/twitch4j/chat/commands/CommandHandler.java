package me.philippheuer.twitch4j.chat.commands;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.enums.TwitchScopes;
import me.philippheuer.twitch4j.events.EventSubscriber;
import me.philippheuer.twitch4j.events.event.MessageEvent;

import javax.swing.text.html.Option;
import java.util.*;

@Getter
@Setter
public class CommandHandler {

	/**
	 * Holds the Twitch Instance
	 */
	private TwitchClient twitchClient;


	/**
	 * Holds all available Commands
	 */
	private HashMap<String, Command> commandMap = new HashMap<String, Command>();

	/**
	 * A map routing command aliases to the primary command
	 */
	public HashMap<String, String> commandAliasToPrimaryMap = new HashMap<String, String>();

	/**
	 * Constructor
	 * @param twitchClient
	 */
	public CommandHandler(TwitchClient twitchClient) {
		setTwitchClient(twitchClient);
	}

	/**
	 * Register a command in the CommandHandler
	 *
	 * @param commandClass Class to register as new command.
	 */
	public void registerCommand(Class<? extends Command> commandClass) {
		// Check if the user provided an Interface instead of a class
		if (commandClass.isInterface()) {
			Logger.error(this, "Can't register an Interface as Command! [%s]!", commandClass.getSimpleName());
			return;
		}

		// Create instance of commandClass and register it
		try {
			Command instance = (Command) Class.forName(commandClass.getName()).newInstance();
			instance.setTwitchClient(getTwitchClient());

			// Check, if the command already was registered
			if(getCommandMap().containsKey(instance.getCommand())) {
				Logger.error(this, "Can't register an Command! [%s]! Error: Command was already registered!", commandClass.getSimpleName());
				return;
			}

			// Register Command
			if(instance.getCommand().length() > 0) {
				// Add to CommandMap
				getCommandMap().put(instance.getCommand(), instance);

				// Add Primary and Aliases to AliasMap
				getCommandAliasToPrimaryMap().put(instance.getCommand(), instance.getCommand());
				for (String cmd : instance.getCommandAliases()) {
					getCommandAliasToPrimaryMap().put(cmd, instance.getCommand());
				}
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
			Logger.error(this, "Can't register an Command! [%s]! Error: %s", commandClass.getSimpleName(), ex.getMessage());
			ex.printStackTrace();
			return;
		}

		Logger.info(this, "Registered new Command [%s]!", commandClass.getSimpleName());
	}

	/**
	 * Entry point for command handling. If the user doesn't have the required
	 * privileges, the command is rejected without a message.
	 *
	 * @param messageEvent The message event. Can infer channel, user, etc.
	 */
	@EventSubscriber
	public void processCommand(MessageEvent messageEvent) {
		// Get real command name from alias
		String cmdName = getCommandAliasToPrimaryMap().get(messageEvent.getMessage().substring("!".length(), messageEvent.getMessage().length()));
		if (messageEvent.getMessage().contains(" ")) {
			cmdName = getCommandAliasToPrimaryMap().get(messageEvent.getMessage().substring("".length(), messageEvent.getMessage().indexOf(" ")));
		}

		// Command existing, or a general message?
		if (cmdName == null) {
			return;
		}

		Optional<Command> cmd = getCommand(cmdName);
		// Command exists?
		if(cmd.isPresent()) {
			Logger.info(this, "Recieved command [%s] from user [%s].!", messageEvent.getMessage(), messageEvent.getUser().getDisplayName());

			// Check Command Permissions
			if (cmd.get().hasPermissions(messageEvent)) {
				cmd.get().executeCommand(messageEvent);
			} else {
				Logger.info(this, "Access to command [%s] denied for user [%s]!", cmdName, messageEvent.getUser().getDisplayName());
			}
		}
	}

	/**
	 * Get Primary Command by Alias or Primary Command
	 */
	public Optional<Command> getCommand(String name) {
		if(getCommandAliasToPrimaryMap().containsKey(name)) {
			return Optional.ofNullable(getCommandMap().get(getCommandAliasToPrimaryMap().get(name)));
		}

		return Optional.empty();
	}

	/**
	 * Get all Commands
	 */
	public Collection<Command> getAllCommands() {
		return new ArrayList<Command>(commandMap.values());
	}
}
