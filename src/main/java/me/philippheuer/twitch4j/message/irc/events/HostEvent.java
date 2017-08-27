package me.philippheuer.twitch4j.message.irc.events;

import lombok.Getter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.message.irc.IRCParser;
import me.philippheuer.twitch4j.model.Channel;

@Getter
public class HostEvent {
	private final Channel channel;
	private final Channel targetChannel;
	private final int viewers;

	private final boolean autohost;

	public HostEvent(TwitchClient client, IRCParser parser) {
		autohost = parser.getMessage().contains("auto");
		String[] msg = parser.getMessage().split(" ");
		if (parser.getMessage().contains("hosting you")) {
			targetChannel = client.getChannelEndpoint(msg[0]).getChannel();
		} else {
			if (!msg[0].contains("-")) targetChannel = client.getChannelEndpoint(msg[0]).getChannel();
			else targetChannel = null;
		}
		this.channel = client.getChannelEndpoint(parser.getChannelName()).getChannel();
		if (!autohost)
			this.viewers = (parser.getMessage().contains("hosting you for")) ? extractNumber(parser.getMessage()) : Integer.parseInt(msg[1]);
		else this.viewers = -1; // Issue: autohost doesn't provide viewers count
	}

	private int extractNumber(String message) {
		return Integer.parseInt(message.replaceAll("\\D+", ""));
	}
}
