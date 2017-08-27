package me.philippheuer.twitch4j.message.irc.events;

import lombok.Getter;
import me.philippheuer.twitch4j.message.irc.IRCParser;

@Getter
public class ClearChatEvent {
	private final String channel;
	private final String user;
	private final String reason;

	public ClearChatEvent(IRCParser parser) {
		channel = parser.getChannelName();
		user = parser.getUserName();
		reason = parser.getTags().getTag("ban-reason").toString()
				.replace("\\\\s", " ")
				.replace("\\\\:", ";")
				.replace("\\\\\\\\", "\\")
				.replace("\\r", "\r")
				.replace("\\n", "\n");
	}
}
