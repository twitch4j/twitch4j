package me.philippheuer.twitch4j.message.irc.events;

import lombok.Getter;

@Getter
public class ChannelState {
	private final String channel;
	private final boolean enabled;

	public ChannelState(String channel, boolean state) {
		this.channel = channel;
		this.enabled = state;
	}
}
