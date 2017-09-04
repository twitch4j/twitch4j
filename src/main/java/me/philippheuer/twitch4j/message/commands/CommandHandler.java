package me.philippheuer.twitch4j.message.commands;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.events.EventSubscriber;
import me.philippheuer.twitch4j.events.event.UnknownCommandEvent;
import me.philippheuer.twitch4j.events.event.irc.ChannelMessageEvent;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class CommandHandler {

	/**
	 * A map routing command aliases to the primary command
	 */
	public HashMap<String, String> commandAliasToPrimaryMap = new HashMap<String, String>();

	/**
	 * Holds the Twitch Instance
	 */
	private TwitchClient twitchClient;

	/**
	 * Holds all available Commands
	 */
	private HashMap<String, Command> commandMap = new HashMap<String, Command>();

	/**
	 * Command Trigger
	 */
	private String commandTrigger = "!";

	/**
	 * Holds the credentials
	 */
	private File commandSaveFile;

	/**
	 * Custom Permissions
	 */
	private Map<Long, List<CommandPermission>> customPermissions = new HashMap<Long, List<CommandPermission>>();

	/**
	 * Class Constructor
	 *
	 * @param twitchClient Instance of TwitchClient
	 * @see TwitchClient
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
			Command command = (Command) Class.forName(commandClass.getName()).newInstance();
			command.setTwitchClient(getTwitchClient());

			// Check, if the command already was registered
			if (getCommandMap().containsKey(command.getCommand())) {
				Logger.error(this, "Can't register an Command! [%s]! Error: Command was already registered!", commandClass.getSimpleName());
				return;
			}

			// Register Command
			if (command.getCommand().length() > 0) {
				// Add to CommandMap
				getCommandMap().put(command.getCommand(), command);

				// Add Primary and Aliases to AliasMap
				getCommandAliasToPrimaryMap().put(command.getCommand(), command.getCommand());
				for (String cmdAlias : command.getCommandAliases()) {
					getCommandAliasToPrimaryMap().put(cmdAlias, command.getCommand());
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
	 * Register a command in the CommandHandler
	 *
	 * @param command Command instance to register.
	 */
	public void registerCommand(Command command) {
		// Create instance of commandClass and register it
		command.setTwitchClient(getTwitchClient());

		// Check, if the command already was registered
		if (getCommandMap().containsKey(command.getCommand())) {
			Logger.error(this, "Can't register Command! [%s]! Error: Command was already registered!", command.getCommand());
			return;
		}

		// Register Command
		if (command.getCommand().length() > 0) {
			// Add to CommandMap
			getCommandMap().put(command.getCommand(), command);

			// Add Primary and Aliases to AliasMap
			getCommandAliasToPrimaryMap().put(command.getCommand(), command.getCommand());
			for (String cmdAlias : command.getCommandAliases()) {
				getCommandAliasToPrimaryMap().put(cmdAlias, command.getCommand());
			}

			// Generally only dynamic commands will be registered using this, so we can save them here
			saveDynamicCommands();
		}


		Logger.info(this, "Registered new Command [%s]!", command.getCommand());
	}

	/**
	 * Unregister a command in the CommandHandler
	 *
	 * @param command Command instance to unregister.
	 */
	public void unregisterCommand(Command command) {
		// Remove from CommandMap
		getCommandMap().remove(command.getCommand(), command);

		// Remove Primary and Aliases from AliasMap
		getCommandAliasToPrimaryMap().put(command.getCommand(), command.getCommand());
		for (String cmdAlias : command.getCommandAliases()) {
			getCommandAliasToPrimaryMap().remove(cmdAlias, command.getCommand());
		}

		// Save Dynamic Commands
		saveDynamicCommands();
	}

	/**
	 * Entry point for command handling. If the user doesn't have the required
	 * privileges, the command is rejected without a message.
	 *
	 * @param messageEvent The message event. Can infer channel, user, etc.
	 */
	@EventSubscriber
	public void processCommand(ChannelMessageEvent messageEvent) {
		String cmdTrigger = messageEvent.getMessage().substring(0, getCommandTrigger().length());
		String cmdName = null;

		if(messageEvent.getMessage().startsWith(getCommandTrigger())) {
			// Get real command name from alias - With Trigger
			if (messageEvent.getMessage().contains(" ")) {
				cmdName = getCommandAliasToPrimaryMap().get(messageEvent.getMessage().substring(getCommandTrigger().length(), messageEvent.getMessage().indexOf(" ")));
			} else {
				cmdName = getCommandAliasToPrimaryMap().get(messageEvent.getMessage().substring(getCommandTrigger().length(), messageEvent.getMessage().length()));
			}
		} else {
			// Get real command name from alias - Without Trigger
			if (messageEvent.getMessage().contains(" ")) {
				cmdName = getCommandAliasToPrimaryMap().get(messageEvent.getMessage().substring(0, messageEvent.getMessage().indexOf(" ")));
			} else {
				cmdName = getCommandAliasToPrimaryMap().get(messageEvent.getMessage().substring(0, messageEvent.getMessage().length()));
			}

			cmdTrigger = "";
		}

		Optional<Command> cmd = getCommand(cmdName);
		// Command exists?
		if (cmd.isPresent()) {
			// Enabled?
			if (cmd.get().getEnabled()) {
				// Check for Command Trigger
				if(cmd.get().getRequiresCommandTrigger()) {
					if(!cmdTrigger.equals(getCommandTrigger())) {
						// Invalid CommandTrigger
						return;
					}
				} else {
					if(!cmdTrigger.equals(getCommandTrigger()) && !cmdTrigger.equals("")) {
						// Only accepting the right or no CommandTrigger
						return;
					}
				}

				// Check for custom defined permissions
				if(customPermissions.containsKey(messageEvent.getUser().getId())) {
					List<CommandPermission> userCustomPermissions = customPermissions.get(messageEvent.getUser().getId());

					messageEvent.getPermissions().addAll(userCustomPermissions);
				}

				// Check Command Permissions
				if (cmd.get().hasPermissions(messageEvent)) {
					Logger.info(this, "Recieved command [%s] from user [%s].!", messageEvent.getMessage(), messageEvent.getUser().getDisplayName());

					cmd.get().executeCommand(messageEvent);
				} else {
					Logger.info(this, "Access to command [%s] denied for user [%s]! (Missing Permissions)", cmdName, messageEvent.getUser().getDisplayName());
				}
			} else {
				Logger.info(this, "Access to command [%s] denied for user [%s].! (Command Disabled)", messageEvent.getMessage(), messageEvent.getUser().getDisplayName());
			}
		} else {
			if(cmdTrigger.equals(getCommandTrigger())) {
				// Dispatch UnknownCommandEvent
				Event event = new UnknownCommandEvent(messageEvent.getChannel(), messageEvent.getUser(), messageEvent);
				getTwitchClient().getDispatcher().dispatch(event);
			}
		}
	}

	/**
	 * Get Primary Command by Alias or Primary Command
	 *
	 * @param name Name or alias of the command.
	 * @return Returns an optional Command instance, that only isPresent if it exists.
	 * @see Command
	 */
	public Optional<Command> getCommand(String name) {
		if (getCommandAliasToPrimaryMap().containsKey(name)) {
			return Optional.ofNullable(getCommandMap().get(getCommandAliasToPrimaryMap().get(name)));
		}

		return Optional.empty();
	}

	/**
	 * Get all Commands
	 *
	 * @return Returns a list of all registered commands. This includes disabled commands.
	 */
	public Collection<Command> getAllCommands() {
		return new ArrayList<Command>(getCommandMap().values());
	}

	/**
	 * Initalizes the Configuration (creates the files)
	 */
	public void initializeConfiguration() {
		// Ensure that the file exists
		try {
			File file = new File(getTwitchClient().getConfigurationDirectory().getAbsolutePath() + File.separator + "commands.json");
			file.createNewFile();
			setCommandSaveFile(file);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// Try to load configuration
		loadDynamicCommands();
	}

	/**
	 * Save Dynamic Commands
	 */
	public void saveDynamicCommands() {
		try {
			// Get all Dynamic Commands
			List<Command> commandList = getCommandMap().values().stream().filter(e -> e instanceof DynamicCommand).collect(Collectors.toList());

			// Save List to File
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(getCommandSaveFile(), commandList);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Load Dynamic Commands
	 */
	public void loadDynamicCommands() {
		purgeDynamicCommands();

		try {
			// Load commands from File
			ObjectMapper mapper = new ObjectMapper();
			List<DynamicCommand> loadedCommands = mapper.readValue(getCommandSaveFile(), new TypeReference<ArrayList<DynamicCommand>>() {
			});

			// Register Commands
			for (Command cmd : loadedCommands) {
				registerCommand(cmd);
			}
		} catch (Exception ex) {
			// we can ignore this, no dynamic content's saved
			if (ex.getMessage().contains("No content")) {
				return;
			}

			ex.printStackTrace();
		}
	}

	public void purgeDynamicCommands() {
		// Get all Dynamic Commands
		List<Command> commandList = getCommandMap().values().stream().filter(e -> e instanceof DynamicCommand).collect(Collectors.toList());

		// Remove all dynamic commands
		for (Command cmd : commandList) {
			getCommandMap().remove(cmd.getCommand());
		}
	}

	/**
	 * Method to grant custom permissions (Owner,Broadcaster,...)
	 *
	 * @param userId Twitch User Id
	 * @param permission Permission
	 */
	public void grantCustomPermission(Long userId, CommandPermission permission) {
		if(customPermissions.containsKey(userId)) {
			customPermissions.get(userId).add(permission);
		} else {
			List<CommandPermission> permissions = new ArrayList<CommandPermission>();
			permissions.add(permission);
			customPermissions.put(userId, permissions);
		}
	}
}
