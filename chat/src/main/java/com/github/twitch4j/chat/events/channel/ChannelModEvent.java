package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

/**
 * This event gets called when a client gains/loses mod status.
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class ChannelModEvent extends AbstractChannelEvent {

	/**
	 * User
	 */
	private EventUser user;

	/**
	 * Is Moderator?
	 */
	private boolean isMod;

	/**
	 * Event Constructor
	 *
	 * @param channel     The channel that this event originates from.
	 * @param user 		  The user that gained/lost mod status.
	 * @param isMod		  Did the use gain or lose mod status?
	 */
	public ChannelModEvent(EventChannel channel, EventUser user, boolean isMod) {
		super(channel);
		this.user = user;
		this.isMod = isMod;
	}
}
