package me.philippheuer.twitch4j.message.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.events.event.irc.ChannelMessageEvent;
import me.philippheuer.twitch4j.model.User;
import me.philippheuer.util.conversion.TypeConvert;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

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
	@JsonIgnore
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
	@JsonIgnore
	protected User actor;

	/**
	 * Command Content
	 * Runtime only
	 */
	@JsonIgnore
	protected String parsedContent;

	/**
	 * Parsed Arguments
	 */
	@Argument
	@JsonIgnore
	private List<String> parsedArguments = new ArrayList<String>();

	/**
	 * This method will use args4j to parse the provided arguments and
	 * will store information about the actor temporary.
	 *
	 * @param messageEvent The MessageEvent triggering this command.
	 * @return True, if parsing was successful. False, if the parsing failed because of missing required arguments, ...
	 */
	public boolean parseArguments(ChannelMessageEvent messageEvent) {
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
	public void executeCommand(ChannelMessageEvent messageEvent) {
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
	public Boolean hasPermissions(ChannelMessageEvent messageEvent) {
		for (CommandPermission permission : messageEvent.getPermissions()) {
			if (getRequiredPermissions().contains(permission)) {
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
	public String getCommandContent(ChannelMessageEvent messageEvent) {
		return TypeConvert.combineStringArray(TypeConvert.removeFirstArrayEntry(messageEvent.getMessage().split(" ")), " ");
	}


	public String[] getCommandArgumentSeperatedList(String regexprSeperator) {

		return getParsedContent().split(String.format("\\s%s\\s", regexprSeperator));
	}

	/**
	 * Gets a list of all usernames, that have been mentioned in the command arguments.
	 *
	 * @return Returns a {@link List} of type string that contains all usernames, that have been mentioned in the message.
	 */
	public List<User> getCommandArgumentTargetUsers() {
		Pattern patternMention = Pattern.compile("@[a-zA-Z0-9_]{4,25}"); // @[a-zA-Z0-9_]{4,25}

		List<User> targetUserList = new ArrayList<User>();
		List<String> targetUserNameList = getParsedArguments().stream().filter(patternMention.asPredicate()).map(map -> map.replace("@", "")).collect(Collectors.toList());

		for (String userName : targetUserNameList) {
			Optional<User> targetUser = getTwitchClient().getUserEndpoint().getUserByUserName(userName);

			// Username Valid?
			// Add to Targets
			targetUser.ifPresent(targetUserList::add);
		}

		return targetUserList;
	}

	/**
	 * Gets the target user of a command, returns the actor (self) if not target.
	 *
	 * @return Instance of type User
	 * @see User
	 */
	public User getCommandArgumentTargetUserOrSelf() {
		List<User> targetUsers = getCommandArgumentTargetUsers();

		if (targetUsers.size() == 1) {
			return targetUsers.get(0);
		} else {
			return getActor();
		}
	}

	/**
	 * Allows to easily send messages to the channel
	 *
	 * @param channelName Name of the channel that should receive the message.
	 * @param message     The message to send to the specified channel.
	 */
	public void sendMessageToChannel(String channelName, String message) {
		getTwitchClient().getMessageInterface().sendMessage(channelName, message);
	}

	/**
	 * Allows to easily send messages to the channel
	 *
	 * @param userName Name of the user that should receive the message.
	 * @param message  The message to send to the specified channel.
	 */
	public void sendMessageToUser(String userName, String message) {
		getTwitchClient().getMessageInterface().sendPrivateMessage(userName, message);
	}


	public void onInvalidCommandUsage(ChannelMessageEvent messageEvent) {

	}
}
