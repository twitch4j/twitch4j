package me.philippheuer.twitch4j.message.irc.events;

import lombok.Getter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.message.irc.IRCParser;

@Getter
public class CheerEvent extends ChatEvent {

	public CheerEvent(TwitchClient client, IRCParser parser) {
		super(client, parser);
	}

}
