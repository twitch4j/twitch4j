package me.philippheuer.twitch4j.chat.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.events.event.MessageEvent;
import me.philippheuer.util.conversion.TypeConvert;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.util.ArrayList;
import java.util.List;
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
	//protected Permission requiredPermission;

	/**
	 * Requires the use of the command trigger
	 */
	protected Boolean requiresCommandTrigger = true;

	/**
	 * An example on how to use the command
	 */
	protected String usageExample;

	/**
	 * Command Content
	 * Only available at execution
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
		CmdLineParser parser = new CmdLineParser(this);

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

	;

	/**
	 * Check if user has permissions
	 *
	 * @param messageEvent The message event. Can infer channel, user, etc.
	 * @return True, if user has the required permissions, false if not.
	 */
	public boolean hasPermissions(MessageEvent messageEvent) {
		// TODO: Permission check (Anyone,Follower,Subscriber,Moderator,Owner)
		return true;
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
	 */
	public List<String> getCommandArgumentTargetUser() {
		return getParsedArguments();
	}

	public void onInvalidCommandUsage(MessageEvent messageEvent) {

	}
}
