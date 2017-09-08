package me.philippheuer.twitch4j.message.irc.parsers.commands.messages;

import lombok.Getter;

@Getter
public class DefaultMessage implements ArgumentMessage {

	private final String channel;
	private final String message;
	private final boolean action;

	public DefaultMessage(String[] args) {
		channel = args[0].substring(1);

		String msg = String.join(" ", args).replace("#" + channel + " :", "");
		if (msg.startsWith("\001") && msg.endsWith("\001")) {
			msg = msg.substring(1, msg.length() - 1);
			action = msg.startsWith("ACTION");
			message = msg.substring("ACTION".length() + 1);
		} else {
			action = false;
			message = msg;
		}
	}

	@Override
	public String toString() {
		return String.format("#%s %s%s", channel, (!action && !message.isEmpty()) ? ":" : "", message);
	}
}
