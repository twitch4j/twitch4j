package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class ChannelStateEvent extends AbstractChannelEvent {
    public enum ChannelState {
        @Deprecated
        BROADCAST_LANG,
        EMOTE,
        FOLLOWERS,
        R9K,
        @Unofficial
        @Deprecated
        RITUALS,
        SLOW,
        SUBSCRIBERS
    }

    private final Map<ChannelState, Object> states;

    /**
     * Event Constructor
     *
     * @param channel The channel that this event originates from.
     * @param state   The changed state triggering the event
     * @param value   The value representing the state
     */
    public ChannelStateEvent(EventChannel channel, ChannelState state, Object value) {
        super(channel);
        Map<ChannelState, Object> states = new HashMap<>();
        states.put(state, value);
        this.states = Collections.unmodifiableMap(states);
    }

    public ChannelStateEvent(EventChannel channel, Map<ChannelState, Object> state) {
        super(channel);
        Map<ChannelState, Object> states = new HashMap<>(state);
        this.states = Collections.unmodifiableMap(states);
    }

    public Object getState(ChannelState state) {
        return states.getOrDefault(state, null);
    }

}
