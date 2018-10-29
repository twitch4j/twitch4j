package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import com.github.twitch4j.chat.domain.ChatChannel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class ChannelStateEvent extends AbstractChannelEvent {
	public enum ChannelState {
		BROADCAST_LANG,
		EMOTE,
		FOLLOWERS,
		R9K,
		SLOW,
		SUBSCRIBERS;
	}

	private final Map<ChannelState, Object> states;

	/**
	 * Event Constructor
	 *
	 * @param channel The channel that this event originates from.
	 */
	public ChannelStateEvent(ChatChannel channel, ChannelState state, Object value) {
		super(channel);
		Map<ChannelState, Object> states = new HashMap<>();
		states.put(state, value);
		this.states = Collections.unmodifiableMap(states);
	}
	public ChannelStateEvent(ChatChannel channel, Map<ChannelState, Object> state) {
		super(channel);
		Map<ChannelState, Object> states = new HashMap<>();
		states.putAll(state);
		this.states = Collections.unmodifiableMap(states);
	}

	public Object getState(ChannelState state) {
		return states.getOrDefault(state, null);
	}
}
