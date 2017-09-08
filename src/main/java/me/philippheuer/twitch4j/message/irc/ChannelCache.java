package me.philippheuer.twitch4j.message.irc;

import lombok.Getter;
import me.philippheuer.twitch4j.message.commands.CommandPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChannelCache {
	private final TwitchChat chat;
	private final String channel;

	@Getter
	private final List<CommandPermission> permissions = new ArrayList<CommandPermission>();
	@Getter
	private final Map<String, List<CommandPermission>> chatterList = new HashMap<String, List<CommandPermission>>();
	@Getter
	private final Map<String, ?> channelStates = new HashMap<>();

	public ChannelCache(TwitchChat chat, String channel) {
		this.chat = chat;
		this.channel = channel;
	}

	public void timeout(String user, int seconds, String reason) {
		sendMessage(String.format(".timeout %s %d%s", user, seconds, (reason!= null) ? " " + reason : ""));
	}
	public void timeout(String user, int seconds) {
		timeout(user, seconds, null);
	}

	public void ban(String user, String reason) {
		sendMessage(String.format(".ban %s%s", user, (reason!= null) ? " " + reason : ""));
	}
	public void ban(String user) {
		ban(user, null);
	}

	public void clearChat() {
		sendMessage(".purge");
	}

	public void unban(String user) {
		sendMessage(".unban " + user);
	}

	public void sendActionMessage(String message) {
		sendMessage(String.format(".me %s", message));
	}

	public void sendMessage(String message) {
		chat.sendMessage(channel, message);
	}
}
