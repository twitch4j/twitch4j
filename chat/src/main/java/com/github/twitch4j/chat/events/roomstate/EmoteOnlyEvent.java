package com.github.twitch4j.chat.events.roomstate;

import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

/**
 * Emote Only State Event
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class EmoteOnlyEvent extends ChannelStatesEvent{

    /**
     * Constructor
     *
     * @param channel ChatChannel
     * @param active State active?
     */
	public EmoteOnlyEvent(EventChannel channel, boolean active) {
		super(channel, active);
	}
}
