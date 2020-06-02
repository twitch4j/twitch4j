package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

/**
 * This event gets called when a user gets banned.
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class UserBanEvent extends AbstractChannelEvent {

	/**
	 * Event Target User
	 */
	private EventUser user;

	/**
	 * Reason for Punishment
	 */
	@Deprecated
	private String reason;

	/**
	 * Event Constructor
	 *
	 * @param channel The channel that this event originates from.
	 * @param user    The user who triggered the event.
	 * @param reason  Reason for Ban.
	 */
	public UserBanEvent(EventChannel channel, EventUser user, String reason) {
		super(channel);
		this.user = user;
		this.reason = reason;
	}
}
