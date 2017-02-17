package me.philippheuer.twitch4j.chat.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.enums.CommandPermission;
import me.philippheuer.twitch4j.events.event.MessageEvent;
import me.philippheuer.twitch4j.model.User;
import me.philippheuer.util.conversion.TypeConvert;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.beans.Transient;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public abstract class Command {
	/**
	 * TwitchClient
	 */
	protected TwitchClient twitchClient;

	/**
	 * Default command syntax
	 */
	protected String command;

	/**
	 * Get aliases of command
	 */
	protected String[] commandAliases;

	/**
	 * Category of the Command
	 */
	protected String category;

	/**
	 * Get a help/description text
	 */
	protected String description;

	/**
	 * Required permission to execute the command
	 */
	protected Set<CommandPermission> requiredPermissions = new HashSet<CommandPermission>();

	/**
	 * Requires the use of the command trigger
	 */
	protected Boolean requiresCommandTrigger = true;

	/**
	 * An example on how to use the command
	 */
	protected String usageExample;

	/**
	 * Enabled?
	 */
	protected Boolean enabled = true;

	/**
	 * Command Actor
	 * Runtime only
	 */
	protected User actor;

	/**
	 * Command Content
	 * Runtime only
	 */
	protected String parsedContent;

	/**
	 * Parsed Arguments
	 */
	@Argument
	private List<String> parsedArguments = new ArrayList<String>();

	/**
	 * Validate arguments
	 */
	public boolean parseArguments(MessageEvent messageEvent) {
		// Save Actor
		setActor(messageEvent.getUser());

		// Parse Arguments
		CmdLineParser parser = new CmdLineParser(this);

		// Disable parsing @ as files (is used to mention users)
		parser.getProperties().withAtSyntax(false);

		try {
			// Parse the arguments.
			parser.parseArgument(messageEvent.getMessage().split(" "));
			setParsedContent(getCommandContent(messageEvent));

			// Remove the Command from the parsedArguments
			getParsedArguments().remove(0);

		} catch (CmdLineException ex) {
			ex.printStackTrace();

			// Invalid Command Usage - Trigger Help?
			return false;
		}

		return true;
	}

	;

	/**
	 * Execute this command
	 *
	 * @param messageEvent The message event. Can infer channel, user, etc.
	 */
	public void executeCommand(MessageEvent messageEvent) {
		if (parseArguments(messageEvent)) {
			// Call Child executeCommand
		} else {
			return;
		}
	}

	/**
	 * Checks if the user has any of the required permissions
	 *
	 * @param messageEvent The message event. Can infer channel, user, etc.
	 * @return True, if user has the required permissions, false if not.
	 */
	public Boolean hasPermissions(MessageEvent messageEvent) {
		for(CommandPermission permission : messageEvent.getPermissions()) {
			if(getRequiredPermissions().contains(permission)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Get the contents of a command, if it has arguments
	 * Returns everything except the first element of a split among a space
	 *
	 * @param messageEvent The message event. Can infer channel, user, etc.
	 * @return String. Empty if there are no arguments
	 */
	public String getCommandContent(MessageEvent messageEvent) {
		return TypeConvert.combineStringArray(TypeConvert.removeFirstArrayEntry(messageEvent.getMessage().split(" ")), " ");
	}


	public String[] getCommandArgumentSeperatedList(String regexprSeperator) {
		String[] choices = getParsedContent().split(String.format("\\s%s\\s", regexprSeperator));

		return choices;
	}

	/**
	 * Get all Users mentioned in the Command Arguments
	 * @return List<String> All mentioned usernames
	 */
	public List<User> getCommandArgumentTargetUsers() {
		Pattern patternMention = Pattern.compile("\\@[a-zA-Z0-9_]{4,25}"); // @[a-zA-Z0-9_]{4,25}

		List<User> targetUserList = new ArrayList<User>();
		List<String> targetUserNameList = getParsedArguments().stream().filter(patternMention.asPredicate()).map(map -> map.replace("@", "")).collect(Collectors.toList());

		for(String userName : targetUserNameList) {
			Optional<User> targetUser = getTwitchClient().getUserEndpoint().getUserByUserName(userName);

			// Username Valid?
			if(targetUser.isPresent()) {
				// Add to Targets
				targetUserList.add(targetUser.get());
			}
		}

		return targetUserList;
	}

	/**
	 * Gets the target user of a command, returns the actor (self) if not target.
	 * @return
	 */
	public User getCommandArgumentTargetUserOrSelf() {
		List<User> targetUsers = getCommandArgumentTargetUsers();
		System.out.println(targetUsers);

		if(targetUsers.size() == 1) {
			return targetUsers.get(0);
		} else {
			return getActor();
		}
	}

	/**
	 * Allows to easily send messages to the channel
	 * @param channelName
	 * @param message
	 */
	public void sendMessageToChannel(String channelName, String message) {
		getTwitchClient().getIrcClient().sendMessage(channelName, message);
	}

	/**
	 * Allows to easily send messages to the channel
	 * @param userName
	 * @param message
	 */
	public void sendMessageToUser(String userName, String message) {
		getTwitchClient().getIrcClient().sendPrivateMessage(userName, message);
	}



	public void onInvalidCommandUsage(MessageEvent messageEvent) {

	}
}
