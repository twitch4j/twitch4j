package me.philippheuer.twitch4j.message.irc.events;

import lombok.Getter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.message.irc.IRCParser;
@Getter
public class UserStatusEvent {

	private final String channel;
	private final String username;
	private final boolean bot;

	public UserStatusEvent(TwitchClient client, IRCParser parser) {
		this.channel = parser.getChannelName();
		this.username = parser.getUserName();
		this.bot = client.getCredentialManager().getTwitchCredentialsForIRC().get().getUserName().equalsIgnoreCase(parser.getUserName());
	}
}
