package me.philippheuer.twitch4j.message.irc.parsers.commands.messages;

import lombok.Getter;

@Getter
public class PrivateMessage implements ArgumentMessage {

	private final String message;
	private final String user;

	public PrivateMessage(String[] args) {
		user = args[0];
		message = String.join(" ", args).replace(user + " :" ,"");
	}

	@Override
	public String toString() {
		return String.format("%s :%s", user, message);
	}
}
