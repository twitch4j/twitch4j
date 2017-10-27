package me.philippheuer.twitch4j.message.commands;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CommandArgument {

	/**
	 * Argument Name
	 */
	String argumentName;

	/**
	 * Argument Aliases
	 */
	String argumentAlias;

	/**
	 * Argument Validation Type
	 * Supported values: List,Choice,...
	 */
	String argumentValidationType;

	/**
	 * Valid Choices for this Argument
	 * Will only be used if argumentValidationType is LIST
	 */
	List<String> choiceList = new ArrayList<String>();

	/**
	 * Help for this Argument
	 */
	String help;

	/**
	 * Constructor
	 *
	 * @param argumentName           Name of the argument
	 * @param argumentAlias          Aliases of the argument
	 * @param argumentValidationType Validation type for the argument input data
	 * @param help                   help text for the argument
	 */
	public CommandArgument(String argumentName, String argumentAlias, String argumentValidationType, String help) {
		super();
		this.argumentName = argumentName;
		this.argumentAlias = argumentAlias;
		this.argumentValidationType = argumentValidationType;
		this.help = help;
	}

}
