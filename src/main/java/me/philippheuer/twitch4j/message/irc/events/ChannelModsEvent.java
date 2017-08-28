package me.philippheuer.twitch4j.message.irc.events;

import lombok.Getter;
import me.philippheuer.twitch4j.message.irc.IRCParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class ChannelModsEvent {
	private final String channel;
	private final List<String> mods = new ArrayList<String>();

	public ChannelModsEvent(IRCParser parser) {
		this.channel = parser.getChannelName();
		this.mods.addAll(Arrays.asList(parser.getMessage().replace("The moderators of this room are: ", "").split(", ")));
	}

	public boolean hasMod(String username) {
		return mods.contains(username);
	}
}
