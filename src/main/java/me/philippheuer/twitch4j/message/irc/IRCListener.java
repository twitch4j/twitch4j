package me.philippheuer.twitch4j.message.irc;

import lombok.Getter;
import me.philippheuer.twitch4j.TwitchClient;

@Getter
public class IRCListener {

	private final TwitchClient twitchClient;

	public IRCListener(TwitchClient client) {
		this.twitchClient = client;
	}

	public void listen(IRCParser ircParser) {

	}
}
