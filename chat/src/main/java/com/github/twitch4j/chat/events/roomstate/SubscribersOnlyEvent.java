package com.github.twitch4j.chat.events.roomstate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import com.github.twitch4j.chat.domain.ChatChannel;

/**
 * Subscribers Only State Event
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class SubscribersOnlyEvent extends ChannelStatesEvent{

    /**
     * Constructor
     *
     * @param channel ChatChannel
     * @param active State active?
     */
	public SubscribersOnlyEvent(ChatChannel channel, boolean active) {
		super(channel, active);
	}
}
