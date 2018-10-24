package com.github.twitch4j.chat.events.roomstate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import com.github.twitch4j.chat.domain.ChatChannel;

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
	public EmoteOnlyEvent(ChatChannel channel, boolean active) {
		super(channel, active);
	}
}
