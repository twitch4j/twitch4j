package me.philippheuer.twitch4j.message.irc.events;

public class ChannelState {
	public final boolean state;
	private final String channel;

	public ChannelState(String channel, boolean state) {
		this.channel = channel;
		this.state = state;
	}

	public String getChannel() { return channel; }
}
