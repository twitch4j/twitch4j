package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

/**
 * This event gets called when a user get a timeout.
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class UserTimeoutEvent extends AbstractChannelEvent {

	/**
	 * Event Target User
	 */
	private EventUser user;

	/**
	 * Duration in Minutes
	 */
	private Integer duration;

	/**
	 * Reason for Punishment
	 */
	private String reason;

	/**
	 * Event Constructor
	 *
	 * @param channel  The channel that this event originates from.
	 * @param user     The user who triggered the event.
	 * @param duration Timeout Duration in Minutes.
	 * @param reason   Reason for Ban.
	 */
	public UserTimeoutEvent(EventChannel channel, EventUser user, Integer duration, String reason) {
		super(channel);
		this.user = user;
		this.duration = duration;
		this.reason = reason;
	}
}
