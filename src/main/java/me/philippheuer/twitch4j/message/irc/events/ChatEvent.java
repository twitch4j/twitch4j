package me.philippheuer.twitch4j.message.irc.events;

import lombok.Getter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.message.irc.IRCParser;
import me.philippheuer.twitch4j.message.irc.IRCTags;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;

@Getter
public class ChatEvent {
	public enum Types {
		ACTION,
		CHAT,
		WHISPER;

		static Types getType(String name) {
			for(Types type : Types.values()) {
				if (type.name().equalsIgnoreCase(name)) return type;
			}
			return null;
		}
	}

	private final TwitchClient twitchClient;
	private final IRCTags tags;
	private final String message;
	private final Channel channel;
	private final User sender;
	private final Types type;

	public ChatEvent(TwitchClient client, IRCParser parser) {
		twitchClient = client;
		tags = parser.getTags();
		message = parser.getMessage();
		sender = client.getUserEndpoint().getUser(parser.getUserId()).get();
		if (parser.getCommand().equalsIgnoreCase("WHISPER"))
			type = Types.WHISPER;
		else if(parser.getCommand().equalsIgnoreCase("PRIVMSG") &&
				parser.getMatcher().group(7).contains("ACTION"))
			type = Types.ACTION;
		else type = Types.CHAT;
		channel = (parser.getCommand().equalsIgnoreCase("WHISPER")) ? null : client.getChannelEndpoint(parser.getChannelName()).getChannel();
	}
}
