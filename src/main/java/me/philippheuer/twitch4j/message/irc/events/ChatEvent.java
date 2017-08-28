package me.philippheuer.twitch4j.message.irc.events;

import lombok.AccessLevel;
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
		WHISPER
	}
	private final IRCTags tags;
	private final String message;
	private final Channel channel;
	private final User sender;
	private final Types type;

	private final boolean bot;

	@Getter(AccessLevel.NONE)
	private final TwitchClient twitchClient;

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
		bot = twitchClient.getCredentialManager().getTwitchCredentialsForIRC().get().getUserName().equalsIgnoreCase(sender.getType());
	}

	public void replay(String message) {
		switch (type) {
			case ACTION:
			case CHAT:
				twitchClient.getMessageInterface().sendMessage(channel.getName(), message);
				break;
			case WHISPER:
				twitchClient.getMessageInterface().sendPrivateMessage(sender.getName(), message);
				break;
			default:
				break;
		}
	}
}
