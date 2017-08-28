package me.philippheuer.twitch4j.message.irc.events;

import lombok.Getter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.message.irc.IRCParser;
import me.philippheuer.twitch4j.model.Channel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class ChannelEvent {

	private final String msgId;
	private final String message;
	private final String channel;

	public ChannelEvent(IRCParser parser) {
		msgId = parser.getTags().getTag("msg-id").toString();
		message = parser.getMessage();
		channel = parser.getChannelName();
	}

	@Override
	public String toString() {
		String channel = (this.channel != null) ? "#" + this.channel + " " : "";
		StringBuilder sb = new StringBuilder();
		sb.append("[")
				.append(msgId)
				.append("] ")
				.append(channel)
				.append(":")
				.append(getMessage());
		return sb.toString();
	}
}
