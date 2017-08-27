package me.philippheuer.twitch4j.message.irc.events;

import lombok.Getter;
import me.philippheuer.twitch4j.message.irc.IRCParser;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ChannelMods {
	private final String channel;
	private final List<String> mods = new ArrayList<String>();

	public ChannelMods(IRCParser parser) {
		this.channel = parser.getChannelName();
		parser.getMessage();
	}
}
