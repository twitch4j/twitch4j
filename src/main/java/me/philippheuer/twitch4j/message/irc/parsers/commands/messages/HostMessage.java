package me.philippheuer.twitch4j.message.irc.parsers.commands.messages;

import lombok.Getter;

@Getter
public class HostMessage implements ArgumentMessage {

	private final String channel;
	private final String hostedChannel;
	private final int viewers;

	public HostMessage(String[] args) {
		channel = args[0].substring(1);
		hostedChannel = (args[1].equals(":-")) ? null : args[1];
		viewers = Integer.parseInt(args[2].replace("[", "").replace("]", ""));
	}

	@Override
	public String toString() {
		return String.format("#%s %s", channel, (hostedChannel != null) ?
				String.format(" -> #%s [%s]", hostedChannel, viewers) :
				String.format(" [UNHOST] [%s]", viewers)
		);
	}
}
